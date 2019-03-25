package com.fxyan.opengl.geometry.circle;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.base.ModelImpl;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author fxYan
 */
public final class Circle
        extends ModelImpl {

    private final int TRIANGLE_COUNT = 80;
    private final int VERTEX_COUNT = TRIANGLE_COUNT + 2;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private FloatBuffer vertexBuffer;
    private float[] vertex;

    private int programHandle;

    public Circle() {
        vertex = new float[VERTEX_COUNT * VERTEX_COUNT];

        for (int i = 0; i < VERTEX_COUNT; i++) {
            double degree = Math.toRadians(360F / TRIANGLE_COUNT) * i;
            vertex[i * PER_VERTEX_SIZE] = (float) Math.cos(degree);
            vertex[i * PER_VERTEX_SIZE + 1] = (float) Math.sin(degree);
            vertex[i * PER_VERTEX_SIZE + 2] = 0;
        }

        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();

        programHandle = GLESUtils.createAndLinkProgram("geometry/circle/circle.vert", "geometry/circle/circle.frag");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, -5, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, VERTEX_COUNT);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
