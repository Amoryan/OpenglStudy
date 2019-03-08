package com.fxyan.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fxyan.opengl.entity.geometry.IOpenGLObject;

/**
 * @author fxYan
 */
public abstract class OpenGLActivity extends AppCompatActivity {

    private GLSurfaceView surfaceView;
    private BaseRenderer renderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);

        renderer = new BaseRenderer();
        surfaceView.setRenderer(renderer);

        init();
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

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void init();

    protected void setObject(Class<? extends IOpenGLObject> clazz) {
        renderer.setObject(clazz);
    }

}
