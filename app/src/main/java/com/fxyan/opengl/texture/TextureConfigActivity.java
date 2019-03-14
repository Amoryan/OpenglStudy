package com.fxyan.opengl.texture;

import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class TextureConfigActivity
        extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    private int wrapSMode = GLES20.GL_REPEAT;
    private int wrapTMode = GLES20.GL_REPEAT;
    private int minFilter = GLES20.GL_NEAREST;
    private int magFilter = GLES20.GL_LINEAR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_config);

        int[] ids = {
                R.id.sRepeat, R.id.sMirror, R.id.sClamp,
                R.id.tRepeat, R.id.tMirror, R.id.tClamp,
                R.id.minNearest, R.id.minLinear,
                R.id.magNearest, R.id.magLinear
        };
        for (int id : ids) {
            ((RadioButton) findViewById(id)).setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) return;

        switch (buttonView.getId()) {
            case R.id.sRepeat:
                wrapSMode = GLES20.GL_REPEAT;
                break;
            case R.id.sMirror:
                wrapSMode = GLES20.GL_MIRRORED_REPEAT;
                break;
            case R.id.sClamp:
                wrapSMode = GLES20.GL_CLAMP_TO_EDGE;
                break;
            case R.id.tRepeat:
                wrapTMode = GLES20.GL_REPEAT;
                break;
            case R.id.tMirror:
                wrapTMode = GLES20.GL_MIRRORED_REPEAT;
                break;
            case R.id.tClamp:
                wrapTMode = GLES20.GL_CLAMP_TO_EDGE;
                break;
            case R.id.minNearest:
                minFilter = GLES20.GL_NEAREST;
                break;
            case R.id.minLinear:
                minFilter = GLES20.GL_LINEAR;
                break;
            case R.id.magNearest:
                magFilter = GLES20.GL_NEAREST;
                break;
            case R.id.magLinear:
                magFilter = GLES20.GL_LINEAR;
                break;
            default:
        }
    }

    public void start(View view) {
        TextureActivity.open(this, wrapSMode, wrapTMode, minFilter, magFilter);
    }
}
