package com.fxyan.opengl.texture.cube;

import android.opengl.GLSurfaceView;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class CubeTextureActivity
        extends OpenGLActivity
        implements GLSurfaceView.Renderer {

    @Override
    protected IModel getModel() {
        return new CubeTexture(this);
    }

}
