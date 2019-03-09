package com.fxyan.opengl.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.fxyan.opengl.entity.IObject;
import com.fxyan.opengl.entity.geometry.Triangle;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private IObject object;
    private Class<? extends IObject> clazz = Triangle.class;

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);

        setRenderer(this);
    }

    public void setObject(Class<? extends IObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            Constructor<? extends IObject> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = constructor.newInstance();
        } catch (Exception e) {
            object = new Triangle();
        }
        object.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        object.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        object.onDrawFrame(gl);
    }

}