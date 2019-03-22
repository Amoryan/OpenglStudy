package com.fxyan.opengl.texture.feature;

import android.opengl.GLSurfaceView;

import com.fxyan.opengl.BaseActivity;
import com.fxyan.opengl.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public abstract class TextureFeatureActivity
        extends BaseActivity
        implements GLSurfaceView.Renderer {

    private TextureFeature object;
    private GLSurfaceView surfaceView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_texture_feature;
    }

    @Override
    protected void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
    }

    @Override
    protected void initData() {
        object = new TextureFeature(this, getWrapMode());
    }

    @Override
    protected void initEvents() {
    }

    protected abstract int getWrapMode();

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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        object.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        object.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        object.onDrawFrame();
    }
}
