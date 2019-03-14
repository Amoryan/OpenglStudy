package com.fxyan.opengl.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.fxyan.opengl.entity.IObject;
import com.fxyan.opengl.entity.geometry.Cube;
import com.fxyan.opengl.entity.geometry.Triangle;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private boolean isChanged;
    private IObject object;
    private Class<? extends IObject> clazz = Triangle.class;

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);

        setRenderer(this);
    }

    public void setObject(Class<? extends IObject> clazz) {
        isChanged = true;
        this.clazz = clazz;
    }

    public IObject getObject() {
        return object;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (isChanged) {
            try {
                Constructor<? extends IObject> constructor = clazz.getDeclaredConstructor(Context.class);
                constructor.setAccessible(true);
                object = constructor.newInstance(getContext());
            } catch (Exception e) {
                object = new Cube(getContext());
            }
            isChanged = false;
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
