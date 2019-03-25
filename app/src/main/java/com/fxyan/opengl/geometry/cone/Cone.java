package com.fxyan.opengl.geometry.cone;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.fxyan.opengl.base.ModelImpl;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author fxYan
 */
public final class Cone
        extends ModelImpl {

    private final int TRIANGLES = 80;
    private final int VERTEX_COUNT = TRIANGLES + 2;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private FloatBuffer vertexBuffer;
    private float[] vertex;

    private int programHandle;

    public Cone() {
        vertex = new float[VERTEX_COUNT * PER_VERTEX_SIZE];

        // 顶部
        vertex[0] = vertex[2] = 0;
        vertex[1] = 1;

        for (int i = 1; i < VERTEX_COUNT; i++) {
            double degree = Math.toRadians(360F / TRIANGLES * (i - 1));
            vertex[i * PER_VERTEX_SIZE] = ((float) (0.5 * Math.cos(degree)));
            vertex[i * PER_VERTEX_SIZE + 1] = 0;
            vertex[i * PER_VERTEX_SIZE + 2] = ((float) (0.5 * Math.sin(degree)));
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

        programHandle = GLESUtils.createAndLinkProgram("geometry/cone/cone.vert", "geometry/cone/cone.frag");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, -5, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();

        GLES20.glUseProgram(programHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 1, 1);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);

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
