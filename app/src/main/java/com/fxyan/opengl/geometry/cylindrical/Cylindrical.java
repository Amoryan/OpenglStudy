package com.fxyan.opengl.geometry.cylindrical;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.base.ModelImpl;
import com.fxyan.opengl.utils.GLESUtils;

/**
 * @author fxYan
 */
public final class Cylindrical
        extends ModelImpl {

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private int programHandle;

    public Cylindrical() {
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();

        programHandle = GLESUtils.createAndLinkProgram("geometry/cylindrical/cylindrical.vert", "geometry/cylindrical/cylindrical.frag");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, -5, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();

        GLES20.glUseProgram(programHandle);

    }

}
