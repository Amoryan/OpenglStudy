package com.fxyan.opengl.texture.feature;

import android.opengl.GLES20;

import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class LinearActivity
        extends TextureFeatureActivity {

    @Override
    protected int getResId() {
        return R.drawable.ic_filter;
    }

    @Override
    protected int getWrapMode() {
        return GLES20.GL_REPEAT;
    }

    @Override
    protected int getMinFilter() {
        return GLES20.GL_LINEAR;
    }

    @Override
    protected int getMagFilter() {
        return GLES20.GL_LINEAR;
    }
}
