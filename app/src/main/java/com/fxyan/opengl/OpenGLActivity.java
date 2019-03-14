package com.fxyan.opengl;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fxyan.opengl.entity.IObject;
import com.fxyan.opengl.widget.GLView;

/**
 * @author fxYan
 */
public abstract class OpenGLActivity extends AppCompatActivity {

    protected GLView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        surfaceView = findViewById(R.id.surfaceView);

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

    protected void setObject(Class<? extends IObject> clazz) {
        surfaceView.setObject(clazz);
    }

    protected IObject getObject() {
        return surfaceView.getObject();
    }

}
