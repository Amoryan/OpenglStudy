package com.fxyan.opengl.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.BaseActivity;
import com.fxyan.opengl.utils.GLESUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author fxYan
 */
public final class ObjActivity
        extends BaseActivity
        implements GLSurfaceView.Renderer {

    private GLSurfaceView surfaceView;
    private ConcurrentHashMap<String, ObjModel> map = new ConcurrentHashMap<>();
    private Map<String, Integer> textureMap = new HashMap<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    private Bitmap t950;
    private Bitmap diamond;

    private float[] mvpMatrix = new float[16];
    private float[] mvMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int programHandle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ply;
    }

    @Override
    protected void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
    }

    @Override
    protected void initData() {
        readObjFile("obj/戒臂.obj");
        textureMap.put("obj/戒臂.obj", 0);
        readObjFile("obj/花托.obj");
        textureMap.put("obj/花托.obj", 0);
        readObjFile("obj/主石.obj");
        textureMap.put("obj/主石.obj", 1);
        readObjFile("obj/副石.obj");
        textureMap.put("obj/副石.obj", 1);

        t950 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_t950);
        diamond = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_diamond);
    }

    @Override
    protected void initEvents() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        t950.recycle();
        diamond.recycle();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        programHandle = GLESUtils.createAndLinkProgram("obj/obj.vert", "obj/obj.frag");

        int[] textureIds = new int[2];
        GLES20.glGenTextures(2, textureIds, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        for (ObjModel value : map.values()) {
            value.onSurfaceCreated();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 30f, 0f, 0f, -5f, 0f, 1f, 0f);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 50);
        for (ObjModel model : map.values()) {
            model.onSurfaceChanged();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 1, 1);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);
        for (Map.Entry<String, ObjModel> entry : map.entrySet()) {
//            int type = textureMap.get(entry.getKey());
//            if (type == 0) {
//                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, t950, 0);
//            } else {
//                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, diamond, 0);
//            }
            entry.getValue().onDrawFrame(mvpMatrix, programHandle);
        }
    }

    private void readObjFile(String path) {
        Single.create((SingleOnSubscribe<ObjModel>) emitter -> {
            boolean result = false;

            BufferedReader bufr = null;
            ArrayList<Float> vertexList = new ArrayList<>();
            ArrayList<Integer> normaList = new ArrayList<>();
            ArrayList<Integer> indexList = new ArrayList<>();
            try {
                bufr = new BufferedReader(new InputStreamReader(getAssets().open(path)));
                String line;
                while ((line = bufr.readLine()) != null) {
                    String[] split = line.split(" ");
                    if (split.length == 0) continue;
                    if ("v".equals(split[0])) {
                        vertexList.add(Float.valueOf(split[1]));
                        vertexList.add(Float.valueOf(split[2]));
                        vertexList.add(Float.valueOf(split[3]));
                    } else if ("f".equals(split[0])) {
                        for (int i = 1; i < 4; i++) {
                            String[] array = split[i].split("/");
                            if (array.length > 0) {
                                indexList.add(Integer.valueOf(array[0]) - 1);
                            }
                            if (array.length > 2) {
                                normaList.add(Integer.valueOf(array[2]) - 1);
                            }
                        }
                    }
                }

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufr != null) {
                    try {
                        bufr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (result) {
                float[] vertex = new float[vertexList.size()];
                for (int i = 0; i < vertexList.size(); i++) {
                    vertex[i] = vertexList.get(i);
                }
                int[] index = new int[indexList.size()];
                for (int i = 0; i < indexList.size(); i++) {
                    index[i] = indexList.get(i);
                }
                emitter.onSuccess(new ObjModel(context, vertex, index));
            } else {
                emitter.onError(new RuntimeException());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ObjModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(ObjModel objModel) {
                        map.put(path, objModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("fxYan", String.format("路径为 %s 的文件解析失败", path));
                    }
                });
    }

}
