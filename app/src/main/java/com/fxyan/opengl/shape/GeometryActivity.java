package com.fxyan.opengl.shape;

import com.fxyan.opengl.OpenGLActivity;
import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class GeometryActivity extends OpenGLActivity {

    @Override
    protected void init() {
        setRender(new Triangle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_geometry;
    }

}
