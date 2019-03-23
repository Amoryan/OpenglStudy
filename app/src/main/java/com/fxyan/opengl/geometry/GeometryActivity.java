package com.fxyan.opengl.geometry;

import android.opengl.GLSurfaceView;

import com.fxyan.opengl.base.BaseActivity;
import com.fxyan.opengl.R;
import com.fxyan.opengl.base.IModel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public abstract class GeometryActivity
        extends BaseActivity
        implements GLSurfaceView.Renderer {

    private IModel model;

    @Override
    public int getLayoutId() {
        return R.layout.activity_geometry;
    }

    @Override
    protected void initViews() {
        GLSurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
    }

    @Override
    protected void initData() {
        model = getModel();
    }

    protected abstract IModel getModel();

    @Override
    protected void initEvents() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        model.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        model.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        model.onDrawFrame();
    }
}
