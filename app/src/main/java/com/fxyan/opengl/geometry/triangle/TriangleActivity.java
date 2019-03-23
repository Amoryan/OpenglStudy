package com.fxyan.opengl.geometry.triangle;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class TriangleActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Triangle();
    }
}
