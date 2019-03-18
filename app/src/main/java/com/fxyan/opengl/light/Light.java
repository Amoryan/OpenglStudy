package com.fxyan.opengl.light;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.fxyan.opengl.entity.geometry.ObjectImpl;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Light extends ObjectImpl {

    private final int PER_FLOAT_BYTES = 4;
    private final int PER_INT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private final int PER_LIGHT_VERTEX_SIZE = 3;
    private final int PER_LIGHT_VERTEX_STRIDE = PER_LIGHT_VERTEX_SIZE * PER_FLOAT_BYTES;

    // 环境光强度
    private float ambientStrength;
    // 环境光颜色
    private float[] ambientLightColor = {1f, 1f, 1f};

    private FloatBuffer vertexBuffer;
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

    private IntBuffer indexBuffer;
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
            5, 1, 2, 5, 2, 6,
    };

    private FloatBuffer lightPositionBuffer;
    private float[] lightPosition = {0f, 0f, 1f};

    private int programHandle;
    private int lightPositionProgramHandle;

    public Light(Context context) {
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

        lightPositionBuffer = ByteBuffer.allocateDirect(lightPosition.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(lightPosition);
        lightPositionBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        programHandle = GLESUtils.createAndLinkProgram("light/light.vert", "light/light.frag");
        lightPositionProgramHandle = GLESUtils.createAndLinkProgram("light/lightposition.vert", "light/lightposition.frag");
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

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 1, 1);

        int modelMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_ModelMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0);

        int viewMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_ViewMatrix");
        GLES20.glUniformMatrix4fv(viewMatrixHandle, 1, false, viewMatrix, 0);

        int projectionMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_ProjectionMatrix");
        GLES20.glUniformMatrix4fv(projectionMatrixHandle, 1, false, projectionMatrix, 0);

        int ambientLightColorHandle = GLES20.glGetUniformLocation(programHandle, "u_AmbientLightColor");
        GLES20.glUniform3fv(ambientLightColorHandle, 1, ambientLightColor, 0);

        int ambientLightStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_AmbientLightStrength");
        GLES20.glUniform1f(ambientLightStrengthHandle, ambientStrength);

        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
        GLES20.glDisableVertexAttribArray(aPositionHandle);

        GLES20.glUseProgram(lightPositionProgramHandle);

        modelMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ModelMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0);

        viewMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ViewMatrix");
        GLES20.glUniformMatrix4fv(viewMatrixHandle, 1, false, viewMatrix, 0);

        projectionMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ProjectionMatrix");
        GLES20.glUniformMatrix4fv(projectionMatrixHandle, 1, false, projectionMatrix, 0);

        lightPositionBuffer.position(0);
        aPositionHandle = GLES20.glGetAttribLocation(lightPositionProgramHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, PER_LIGHT_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_LIGHT_VERTEX_STRIDE, lightPositionBuffer);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, lightPosition.length);
    }

    public void setAmbientStrength(float value) {
        ambientStrength = value;
    }

    public void setDiffuseStrength(float diffuseStrength) {
    }
}
