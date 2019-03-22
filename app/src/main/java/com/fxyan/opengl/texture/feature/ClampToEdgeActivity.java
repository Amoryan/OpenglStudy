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
}
