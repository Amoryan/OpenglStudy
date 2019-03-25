package com.fxyan.opengl.geometry.cone;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class ConeActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Cone();
    }
}
