package com.fxyan.opengl.texture;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_texture_feature;
    }

    @Override
    protected void initParams(Bundle bundle) {
    }

    @Override
    protected void initViews() {
        GLSurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvents() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
