package com.fxyan.opengl.entity.geometry;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.fxyan.opengl.utils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Cube extends ObjectImpl {

    private final int PER_FLOAT_BYTE = 4;
    private final int PER_INT_BYTE = 4;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_COLOR_SIZE = 4;

    private final int PER_VERTEX_STRIDE = PER_FLOAT_BYTE * PER_VERTEX_SIZE;
    private final int PER_COLOR_STRIDE = PER_FLOAT_BYTE * PER_COLOR_SIZE;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    private float[] vertex = {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,

            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
    };
    private float[] color = {
            0f, 0f, 1f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 1f, 1f
    };
    private int[] index = {
            // front
            0, 1, 2, 0, 2, 3,
            // back
            4, 5, 6, 4, 6, 7,
            // left
            4, 5, 1, 4, 1, 0,
            // right
            3, 2, 6, 3, 6, 7,
            // top
            4, 0, 3, 4, 3, 7,
            // bottom
            5, 1, 2, 5, 2, 6
    };

    private int programHandle;

    public Cube() {
        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * PER_FLOAT_BYTE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * PER_FLOAT_BYTE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(color);
        colorBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(index.length * PER_INT_BYTE)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(index);
        indexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        int vertexShaderHandle = GLUtils.createShader(GLES20.GL_VERTEX_SHADER, "geometry/cube.vert");
        int fragmentShaderHandle = GLUtils.createShader(GLES20.GL_FRAGMENT_SHADER, "geometry/cube.frag");

        programHandle = GLUtils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 1, 1);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        int aColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glVertexAttribPointer(aColorHandle, PER_COLOR_SIZE, GLES20.GL_FLOAT, false, PER_COLOR_STRIDE, colorBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
    }

}
