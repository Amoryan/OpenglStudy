package com.fxyan.opengl.light.texture;

import android.opengl.GLSurfaceView;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class LightTextureActivity
        extends OpenGLActivity
        implements GLSurfaceView.Renderer {

    @Override
    protected IModel getModel() {
        return new LightTexture(this);
    }

}
