package com.fxyan.opengl.blending;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class BlendingActivity
        extends OpenGLActivity {

    @Override
    protected IModel getModel() {
        return new Blending();
    }

}
