package com.fxyan.opengl.geometry.line;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class LineActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Line();
    }
}
