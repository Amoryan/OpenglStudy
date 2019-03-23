package com.fxyan.opengl.geometry.triangle;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.geometry.GeometryActivity;

/**
 * @author fxYan
 */
public final class TriangleActivity
        extends GeometryActivity {
    @Override
    protected IModel getModel() {
        return new Triangle();
    }
}
