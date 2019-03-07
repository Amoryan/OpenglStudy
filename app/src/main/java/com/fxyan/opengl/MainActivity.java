package com.fxyan.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glview = findViewById(R.id.glview);

        glview.setEGLContextClientVersion(2);
        glview.setRenderer(new LessonTwoRender_(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        glview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glview.onPause();
    }
}
