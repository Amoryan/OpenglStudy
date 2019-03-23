package com.fxyan.opengl.geometry.square;

import com.fxyan.opengl.entity.IModel;
import com.fxyan.opengl.entity.geometry.Square;
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
