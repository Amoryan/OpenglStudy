package com.fxyan.opengl.texture.feature;

import android.opengl.GLES20;

/**
 * @author fxYan
 */
public final class ClampToEdgeActivity
        extends TextureFeatureActivity {
    @Override
    protected int getWrapMode() {
        return GLES20.GL_CLAMP_TO_EDGE;
    }

    @Override
    protected int getMinFilter() {
        return GLES20.GL_NEAREST;
    }

    @Override
    protected int getMagFilter() {
        return GLES20.GL_LINEAR;
    }
}
