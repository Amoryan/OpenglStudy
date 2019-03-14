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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class Light extends ObjectImpl {

    private final int PER_FLOAT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 7;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private final int PER_VERTEX_COORD_SIZE = 3;
    private final int PER_VERTEX_COLOR_SIZE = 4;

    private final int PER_NORMAL_SIZE = 3;
    private final int PER_NORMAL_STRIDE = PER_NORMAL_SIZE * PER_FLOAT_BYTES;

    // 环境光强度
    private float ambientStrength;
    // 环境光颜色
    private float[] ambientLightColor = {1f, 1f, 1f};

    // 漫射光颜色
    private float[] diffuseLightColor = {1f, 1f, 1f};
    // 漫射光的强度
    private float diffuseStrength;

    // 光源位置
    private float[] lightPosition = {0f, 0, 1};

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private float[] vertex = {
            // front face
            -0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,

            // back face
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,

            // left face
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,

            // right face
            0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,

            // top face
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, 0.5f, -0.5f, 1f, 1f, 0f, 1f,

            // bottom face
            -0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            -0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 1f, 1f, 0f, 1f,
            0.5f, -0.5f, -0.5f, 1f, 1f, 0f, 1f,
    };
    private float[] normal = {
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,

            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,

            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,

            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,

            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,

            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
    };

    private int programHandle;

    public Light(Context context) {
        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        vertexBuffer.position(0);

        normalBuffer = ByteBuffer.allocateDirect(normal.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(normal);
        normalBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        programHandle = GLESUtils.createAndLinkProgram("light/light.vert", "light/light.frag");
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
        GLES20.glUniform4fv(modelMatrixHandle, 1, modelMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int ambientLightColorHandle = GLES20.glGetUniformLocation(programHandle, "u_AmbientLightColor");
        GLES20.glUniform3fv(ambientLightColorHandle, 1, ambientLightColor, 0);

        int ambientLightStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_AmbientLightStrength");
        GLES20.glUniform1f(ambientLightStrengthHandle, ambientStrength);

        int lightPositionHandle = GLES20.glGetUniformLocation(programHandle, "u_LightPosition");
        GLES20.glUniform3fv(lightPositionHandle, 1, lightPosition, 0);

        int diffuseLightColorHandle = GLES20.glGetUniformLocation(programHandle, "u_DiffuseLightColor");
        GLES20.glUniform3fv(diffuseLightColorHandle, 1, diffuseLightColor, 0);

        int diffuseLightStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_DiffuseLightStrength");
        GLES20.glUniform1f(diffuseLightStrengthHandle, diffuseStrength);

        vertexBuffer.position(0);
        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, PER_VERTEX_COORD_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        vertexBuffer.position(PER_VERTEX_COORD_SIZE);
        int aColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glVertexAttribPointer(aColorHandle, PER_VERTEX_COLOR_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        normalBuffer.position(0);
        int aNormalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
        GLES20.glEnableVertexAttribArray(aNormalHandle);
        GLES20.glVertexAttribPointer(aNormalHandle, PER_NORMAL_SIZE, GLES20.GL_FLOAT, false, PER_NORMAL_STRIDE, normalBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / PER_VERTEX_SIZE);
    }

    public void setAmbientStrength(float value) {
        ambientStrength = value;
    }

    public void setDiffuseStrength(float diffuseStrength) {
        this.diffuseStrength = diffuseStrength;
    }
}
