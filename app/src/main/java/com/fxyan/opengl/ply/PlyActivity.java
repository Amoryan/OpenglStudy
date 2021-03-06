package com.fxyan.opengl.ply;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.BaseActivity;
import com.fxyan.opengl.utils.GLESUtils;

import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReaderFile;

import java.io.IOException;
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
public final class PlyActivity
        extends BaseActivity
        implements GLSurfaceView.Renderer {

    private GLSurfaceView surfaceView;
    private ConcurrentHashMap<String, PlyModel> map = new ConcurrentHashMap<>();
    private Map<String, Integer> textureMap = new HashMap<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    private Bitmap t950;
    private Bitmap diamond;

    private float[] mvpMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int programHandle;

    private float maxVertex = 1;
    private float currentScale = 0.5f;

    private float downX, downY;
    private float degreeX, degreeY;
    private boolean isMultiPointer;
    private float ratio;
    private ScaleGestureDetector detector;

    @Override
    public int getLayoutId() {
        return R.layout.activity_model;
    }

    @Override
    protected void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
        detector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                scaleFactor = (scaleFactor - 1) * 2 + 1;// 基数放大2倍
                currentScale *= scaleFactor;
                if (currentScale >= 1) {
                    currentScale = 1;
                } else if (currentScale <= 0.2) {
                    currentScale = 0.2f;
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
    }

    @Override
    protected void initData() {
        readPlyFile("ply/戒臂.ply");
        textureMap.put("ply/戒臂.ply", 0);
        readPlyFile("ply/花托.ply");
        textureMap.put("ply/花托.ply", 0);
        readPlyFile("ply/主石.ply");
        textureMap.put("ply/主石.ply", 1);
        readPlyFile("ply/副石.ply");
        textureMap.put("ply/副石.ply", 1);

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

        programHandle = GLESUtils.createAndLinkProgram("ply/ply.vert", "ply/ply.frag");

        int[] textureIds = new int[2];
        GLES20.glGenTextures(2, textureIds, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        for (PlyModel value : map.values()) {
            value.onSurfaceCreated();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;

        for (PlyModel model : map.values()) {
            model.onSurfaceChanged();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);

        float scale = maxVertex / currentScale;
        Matrix.orthoM(projectionMatrix, 0, -ratio * scale, ratio * scale, -scale, scale, -scale, scale);

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, degreeY + angleInDegrees, 0f, 1f, 0f);
        Matrix.rotateM(modelMatrix, 0, degreeX, 1f, 0f, 0f);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        for (Map.Entry<String, PlyModel> entry : map.entrySet()) {
            int type = textureMap.get(entry.getKey());
            if (type == 0) {
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, t950, 0);
            } else {
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, diamond, 0);
            }
            entry.getValue().onDrawFrame(mvpMatrix, programHandle);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                if (pointerCount > 1) {
                    isMultiPointer = true;
                } else if (pointerCount == 1 && !isMultiPointer) {
                    float moveX = event.getX();
                    float moveY = event.getY();
                    degreeX += (moveY - downY) / 2;
                    degreeY += (moveX - downX) / 2;
                    downX = moveX;
                    downY = moveY;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isMultiPointer = false;
                break;
            default:
        }
        return true;
    }

    private void readPlyFile(String path) {
        Single.create((SingleOnSubscribe<PlyModel>) emitter -> {
            boolean result = false;

            PlyReaderFile reader = null;
            float[] vertex = null;
            int[] index = null;
            try {
                reader = new PlyReaderFile(getAssets().open(path));

                vertex = readVertex(reader);
                index = readFace(reader);

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            if (result) {
                emitter.onSuccess(new PlyModel(context, vertex, index));
            } else {
                emitter.onError(new RuntimeException());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<PlyModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(PlyModel plyModel) {
                        map.put(path, plyModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("fxYan", String.format("路径为 %s 的文件解析失败", path));
                    }
                });
    }

    private float[] readVertex(PlyReaderFile reader) throws IOException {
        ElementReader elementReader = reader.nextElementReader();
        float[] vertex = new float[elementReader.getCount() * PlyModel.PER_VERTEX_SIZE];
        for (int i = 0; i < elementReader.getCount(); i++) {
            Element element = elementReader.readElement();
            float x = (float) element.getDouble("x");
            vertex[i * PlyModel.PER_VERTEX_SIZE] = x;
            if (maxVertex < x) {
                maxVertex = x;
            }
            float y = (float) element.getDouble("y");
            vertex[i * PlyModel.PER_VERTEX_SIZE + 1] = y;
            if (maxVertex < y) {
                maxVertex = y;
            }
            float z = (float) element.getDouble("z");
            vertex[i * PlyModel.PER_VERTEX_SIZE + 2] = z;
            if (maxVertex < z) {
                maxVertex = z;
            }
            vertex[i * PlyModel.PER_VERTEX_SIZE + 3] = (float) element.getDouble("nx");
            vertex[i * PlyModel.PER_VERTEX_SIZE + 4] = (float) element.getDouble("ny");
            vertex[i * PlyModel.PER_VERTEX_SIZE + 5] = (float) element.getDouble("nz");
        }
        elementReader.close();
        return vertex;
    }

    private int[] readFace(PlyReaderFile reader) throws IOException {
        ElementReader elementReader = reader.nextElementReader();
        int PER_FACE_VERTEX_COUNT = 3;
        int[] index = new int[elementReader.getCount() * PER_FACE_VERTEX_COUNT];
        for (int i = 0; i < elementReader.getCount(); i++) {
            Element element = elementReader.readElement();
            int[] vertex_indices = element.getIntList("vertex_indices");
            index[i * PER_FACE_VERTEX_COUNT] = vertex_indices[0];
            index[i * PER_FACE_VERTEX_COUNT + 1] = vertex_indices[1];
            index[i * PER_FACE_VERTEX_COUNT + 2] = vertex_indices[2];
        }
        elementReader.close();
        return index;
    }

}
