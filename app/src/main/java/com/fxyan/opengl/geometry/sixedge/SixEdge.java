package com.fxyan.opengl.geometry.sixedge;

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
public final class SixEdge
        extends ModelImpl {

    private final int PER_VERTEX_COUNT = 8;
    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private FloatBuffer vertexBuffer;
    private float[] vertex;

    public SixEdge() {
        double _60d = Math.toRadians(60);
        float sin60 = (float) Math.sin(_60d);
        float cos60 = (float) Math.cos(_60d);

        vertex = new float[PER_VERTEX_COUNT * PER_VERTEX_SIZE];
        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = 0;
        }
        // 第二个点
        vertex[3] = -cos60;
        vertex[4] = -sin60;
        // 第三个点
        vertex[6] = cos60;
        vertex[7] = -sin60;
        // 第四个点
        vertex[9] = 1;
        vertex[10] = 0;
        // 第五个点
        vertex[12] = cos60;
        vertex[13] = sin60;
        // 第六个点
        vertex[15] = -cos60;
        vertex[16] = sin60;
        // 第七个点
        vertex[18] = -1;
        vertex[19] = 0;
        // 第八个点
        vertex[21] = -cos60;
        vertex[22] = -sin60;

        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);
    }

    private int programHandle;

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();

        programHandle = GLESUtils.createAndLinkProgram("geometry/sixedge/sixedge.vert", "geometry/sixedge/sixedge.frag");
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, PER_VERTEX_COUNT);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
