package com.fxyan.opengl.entity.geometry;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.utils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Triangle extends ObjectImpl {

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
        super.onSurfaceCreated(gl, config);
        int vertexShaderHandle = GLUtils.createShader(GLES20.GL_VERTEX_SHADER, "geometry/triangle.vert");
        int fragmentShaderHandle = GLUtils.createShader(GLES20.GL_FRAGMENT_SHADER, "geometry/triangle.frag");

        programHandle = GLUtils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 7 * 4, vertexBuffer);

        int aColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        vertexBuffer.position(3);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 7 * 4, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / 7);
    }

}
