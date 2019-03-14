package com.fxyan.opengl.texture;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class TextureRenderer
        implements GLSurfaceView.Renderer {

    private Texture2D obj;

    public TextureRenderer(Context _context, int _sMode, int _tMode, int _minFilter, int _magFilter) {
        obj = new Texture2D(_context, _sMode, _tMode, _minFilter, _magFilter);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        obj.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        obj.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        obj.onDrawFrame();
    }
}
