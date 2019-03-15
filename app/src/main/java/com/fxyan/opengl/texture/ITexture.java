package com.fxyan.opengl.texture;

/**
 * @author fxYan
 */
public interface ITexture {
    void onSurfaceCreated();

    void onSurfaceChanged(int width, int height);

    void onDrawFrame();
}
