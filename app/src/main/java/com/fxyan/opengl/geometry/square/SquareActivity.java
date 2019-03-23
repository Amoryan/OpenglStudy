package com.fxyan.opengl.geometry.square;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.geometry.GeometryActivity;

/**
 * @author fxYan
 */
public final class SquareActivity
        extends GeometryActivity {
    @Override
    protected IModel getModel() {
        return new Square();
    }
}
