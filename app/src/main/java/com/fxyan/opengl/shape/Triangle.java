package com.fxyan.opengl.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.BaseRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Triangle extends BaseRenderer {

    private FloatBuffer vertexBuffer;
    private float[] vertex = {
            -0.5f, -0.5f, 0.0f, 1f, 0f, 0f, 1f,// left bottom
            0.5f, -0.5f, 0.0f, 0f, 1f, 0f, 1f,// right bottom
            0.0f, 0.5f, 0.0f, 0f, 0f, 1f, 1f// top
    };
    private FloatBuffer colorBuffer;
    private float[] color = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
    };

    private float[] mvpMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int programHandle;

    public Triangle() {
        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(color);
        colorBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1, 0);

        int vertexShaderHandle = createShader(GLES20.GL_VERTEX_SHADER, Config.VERTEX_SHADER_SOURCE);
        int fragmentShaderHandle = createShader(GLES20.GL_FRAGMENT_SHADER, Config.FRAGMENT_SHADER_SOURCE);

        programHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 10f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, Config.U_MVPMATRIX);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, Config.A_POSITION);
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 7 * 4, vertexBuffer);

        int aColorHandle = GLES20.glGetAttribLocation(programHandle, Config.A_COLOR);
        vertexBuffer.position(3);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 7 * 4, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / 3 * 3);
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
