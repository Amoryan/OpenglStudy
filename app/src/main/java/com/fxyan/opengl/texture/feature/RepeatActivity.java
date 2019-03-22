package com.fxyan.opengl.texture.feature;

import android.opengl.GLES20;

/**
 * @author fxYan
 */
public final class RepeatActivity
        extends TextureFeatureActivity {
    @Override
    protected int getWrapMode() {
        return GLES20.GL_REPEAT;
    }
}
