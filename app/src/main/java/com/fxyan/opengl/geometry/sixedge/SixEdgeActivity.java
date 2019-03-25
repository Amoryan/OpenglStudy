package com.fxyan.opengl.geometry.sixedge;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class SixEdgeActivity
        extends OpenGLActivity {
    @Override
    protected IModel getModel() {
        return new SixEdge();
    }
}
