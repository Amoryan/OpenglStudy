package com.fxyan.opengl.geometry;

import android.opengl.GLES20;

import com.fxyan.opengl.BaseRenderer;
import com.fxyan.opengl.entity.geometry.BaseOpenGLObjectImpl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Square extends BaseOpenGLObjectImpl {

    private FloatBuffer buffer;
    private float[] vertex = {
            -0.5f, 0.5f, 0f, 1f, 0f, 0f, 1f,
            -0.5f, -0.5f, 0f, 0f, 1f, 0f, 1f,
            0.5f, -0.5f, 0f, 0f, 0f, 1f, 1f,

            -0.5f, 0.5f, 0f, 1f, 0f, 0f, 1f,
            0.5f, -0.5f, 0f, 0f, 0f, 1f, 1f,
            0.5f, 0.5f, 0f, 1f, 0f, 1f, 1f,
    };

    private int programHandle;

    public Square() {
        buffer = ByteBuffer.allocateDirect(vertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        buffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        int vertexShaderHandle = BaseRenderer.createShader(GLES20.GL_VERTEX_SHADER, Config.VERTEX_SHADER_SOURCE);
        int fragmentShaderHandle = BaseRenderer.createShader(GLES20.GL_FRAGMENT_SHADER, Config.FRAGMENT_SHADER_SOURCE);

        programHandle = BaseRenderer.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, Config.U_MVPMATRIX);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        buffer.position(0);
        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, Config.A_POSITION);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 7 * 4, buffer);

        buffer.position(3);
        int aColorHandle = GLES20.glGetAttribLocation(programHandle, Config.A_COLOR);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 7 * 4, buffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / 7);
    }

    public static class Config {
        public static final String U_MVPMATRIX = "u_MVPMatrix";
        public static final String A_POSITION = "a_Position";
        public static final String A_COLOR = "a_Color";

        public static final String VERTEX_SHADER_SOURCE = "" +
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "attribute vec4 a_Color;" +
                "varying vec4 v_Color;" +
                "void main(){" +
                "   gl_Position = u_MVPMatrix" +
                "               * a_Position;" +
                "   v_Color = a_Color;" +
                "}";

        public static final String FRAGMENT_SHADER_SOURCE = "" +
                "precision mediump float;" +
                "varying vec4 v_Color;" +
                "void main(){" +
                "   gl_FragColor = v_Color;" +
                "}";
    }
}
