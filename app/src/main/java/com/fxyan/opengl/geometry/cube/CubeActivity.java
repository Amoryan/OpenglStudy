package com.fxyan.opengl.geometry.cube;

import com.fxyan.opengl.entity.IModel;
import com.fxyan.opengl.entity.geometry.Cube;
import com.fxyan.opengl.geometry.GeometryActivity;

/**
 * @author fxYan
 */
public final class CubeActivity
        extends GeometryActivity {
    @Override
    protected IModel getModel() {
        return new Cube();
    }
}
