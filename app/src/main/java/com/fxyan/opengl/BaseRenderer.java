package com.fxyan.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * @author fxYan
 */
public abstract class BaseRenderer implements GLSurfaceView.Renderer {

    protected int createShader(int shaderType, String shaderSource) {
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

    protected int createAndLinkProgram(int... shaderList) {
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
