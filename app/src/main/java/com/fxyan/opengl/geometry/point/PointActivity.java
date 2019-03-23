package com.fxyan.opengl.geometry.point;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class PointActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Point();
    }
}
