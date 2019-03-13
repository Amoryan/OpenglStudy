package com.fxyan.opengl.ply;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fxyan.opengl.R;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class PlyActivity
        extends AppCompatActivity
        implements GLSurfaceView.Renderer {

    GLSurfaceView surfaceView;
    List<PlyModel> models = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ply);

        surfaceView = findViewById(R.id.surfaceView);

        surfaceView.setEGLContextClientVersion(2);

        surfaceView.setRenderer(this);

        models.add(new PlyModel(this, "ply/主石.ply"));
        models.add(new PlyModel(this, "ply/戒臂2.ply"));
        models.add(new PlyModel(this, "ply/花托2.ply"));
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
