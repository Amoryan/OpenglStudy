package com.fxyan.opengl.geometry.cylindrical;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class CylindricalActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new Cylindrical();
    }
}
