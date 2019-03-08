package com.fxyan.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.fxyan.opengl.entity.IOpenGLObject;
import com.fxyan.opengl.entity.geometry.Triangle;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public class BaseRenderer implements GLSurfaceView.Renderer {

    private IOpenGLObject object;
    private Class<? extends IOpenGLObject> clazz = Triangle.class;

    public void setObject(Class<? extends IOpenGLObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            Constructor<? extends IOpenGLObject> constructor = clazz.getDeclaredConstructor();
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

    public static int createShader(int shaderType, String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }
        if (shaderHandle == 0) {
            throw new RuntimeException("create shader error!");
        }
        return shaderHandle;
    }

    public static int createAndLinkProgram(int... shaderList) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            for (int shader : shaderList) {
                GLES20.glAttachShader(programHandle, shader);
            }
            GLES20.glLinkProgram(programHandle);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("create program error!");
        }
        return programHandle;
    }

}
