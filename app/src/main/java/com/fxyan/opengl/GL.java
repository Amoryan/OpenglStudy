package com.fxyan.opengl;

import android.opengl.GLES20;

/**
 * @author fxYan
 */
public final class GL {

    /**
     * @param shaderType   shader类型
     * @param shaderSource shader源码
     * @return
     */
    public static int createShader(int shaderType, String shaderSource) {
        int shaderObjectId = GLES20.glCreateShader(shaderType);
        if (shaderObjectId != 0) {
            GLES20.glShaderSource(shaderObjectId, shaderSource);
            GLES20.glCompileShader(shaderObjectId);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(shaderObjectId);
                shaderObjectId = 0;
            }
            if (shaderObjectId == 0) {
                throw new RuntimeException(String.format("create shader error, shader type is %s", shaderType));
            }
        }
        return shaderObjectId;
    }

    /**
     * 链接program
     *
     * @param shaderArray 需要attach 的shader
     * @return
     */
    public static int linkProgram(String[] attrs, int... shaderArray) {
        int programObjectId = GLES20.glCreateProgram();
        if (programObjectId != 0) {
            for (int shader : shaderArray) {
                GLES20.glAttachShader(programObjectId, shader);
            }
            for (int i = 0; i < attrs.length; i++) {
                GLES20.glBindAttribLocation(programObjectId, i, attrs[i]);
            }
            GLES20.glLinkProgram(programObjectId);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programObjectId);
                programObjectId = 0;
            }
        }
        if (programObjectId == 0) {
            throw new RuntimeException("create program error");
        }
        return programObjectId;
    }

}
