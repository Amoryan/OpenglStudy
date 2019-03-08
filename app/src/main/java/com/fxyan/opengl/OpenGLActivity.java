package com.fxyan.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author fxYan
 */
public abstract class OpenGLActivity extends AppCompatActivity {

    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);

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

    protected void setRender(GLSurfaceView.Renderer renderer) {
        surfaceView.setRenderer(renderer);
    }

}
