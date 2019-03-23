package com.fxyan.opengl.geometry.circle;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class CircleActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Circle();
    }
}
