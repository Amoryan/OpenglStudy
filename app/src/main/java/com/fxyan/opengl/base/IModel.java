package com.fxyan.opengl.base;

/**
 * @author fxYan
 */
public interface IModel {

    int PER_FLOAT_BYTES = 4;
    int PER_INT_BYTES = 4;

    default void onSurfaceCreated() {
    }

    default void onSurfaceChanged(int width, int height) {
    }

    default void onDrawFrame() {
    }
}
