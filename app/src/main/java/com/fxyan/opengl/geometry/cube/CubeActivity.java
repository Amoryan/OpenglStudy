package com.fxyan.opengl.geometry.cube;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class CubeActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Cube();
    }
}
