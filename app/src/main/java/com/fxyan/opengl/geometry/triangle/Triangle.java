package com.fxyan.opengl.geometry.triangle;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.base.ModelImpl;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author fxYan
 */
public final class Triangle
        extends ModelImpl {

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private FloatBuffer vertexBuffer;
    private float[] vertex = {
            -1.0f, 0f, 0f,
            -0.5f, 0f, 0f,
            -0.75f, 0.5f, 0f,

            -0.25f, 0f, 0f,
            0f, -0.5f, 0f,
            0.25f, 0f, 0f,

            0.5f, 0f, 0f,
            1f, 0f, 0f,
            0.75f, 0.5f, 0f,
    };

    private IntBuffer indexBuffer;
    private int[] index = {
            0, 1, 2,
            3, 4, 5,
            6, 7, 8,
    };

    private int mode = GLES20.GL_TRIANGLES;

    private int programHandle;

    public Triangle() {
        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(index.length * PER_INT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(index);
        indexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        programHandle = GLESUtils.createAndLinkProgram("geometry/triangle/triangle.vert", "geometry/triangle/triangle.frag");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawElements(mode, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
