package com.fxyan.opengl.texture;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class TextureActivity
        extends AppCompatActivity {

    public static final String WRAP_S_MODE = "sMode";
    public static final String WRAP_T_MODE = "tMode";
    public static final String MIN_FILTER = "minFilter";
    public static final String MAG_FILTER = "magFilter";

    public static void open(Context context, int wrapSMode, int wrapTMode, int minFilter, int magFilter) {
        Intent intent = new Intent(context, TextureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WRAP_S_MODE, wrapSMode);
        bundle.putInt(WRAP_T_MODE, wrapTMode);
        bundle.putInt(MIN_FILTER, minFilter);
        bundle.putInt(MAG_FILTER, magFilter);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture);

        Bundle extras = getIntent().getExtras();
        int sMode = extras.getInt(WRAP_S_MODE);
        int tMode = extras.getInt(WRAP_T_MODE);
        int minFilter = extras.getInt(MIN_FILTER);
        int magFilter = extras.getInt(MAG_FILTER);

        GLSurfaceView glView = findViewById(R.id.surfaceView);

        glView.setEGLContextClientVersion(2);

        glView.setRenderer(new TextureRenderer(this, sMode, tMode, minFilter, magFilter));
    }

}
