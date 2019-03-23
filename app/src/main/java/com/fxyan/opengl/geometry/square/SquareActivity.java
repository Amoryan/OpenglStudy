package com.fxyan.opengl.geometry.square;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class SquareActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Square();
    }
}
