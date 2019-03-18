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

    private final int PER_VERTEX_SIZE = 6;
    private final int VERTEX_COORD_START = 0;
    private final int PER_VERTEX_COORD_SIZE = 3;
    private final int VERTEX_NORMAL_START = PER_VERTEX_COORD_SIZE;
    private final int PER_VERTEX_NORMAL_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private final int PER_LIGHT_VERTEX_SIZE = 3;
    private final int PER_LIGHT_VERTEX_STRIDE = PER_LIGHT_VERTEX_SIZE * PER_FLOAT_BYTES;

    // 环境光强度
    private float ambientStrength;
    // 环境光颜色
    private float[] ambientLightColor = {1f, 1f, 1f};

    private FloatBuffer vertexBuffer;
    private float[] vertex = {
            // front
            -0.5f, 0.5f, 0.5f, 0f, 0f, 1f,
            -0.5f, -0.5f, 0.5f, 0f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 0f, 0f, 1f,
            -0.5f, 0.5f, 0.5f, 0f, 0f, 1f,
            0.5f, -0.5f, 0.5f, 0f, 0f, 1f,
            0.5f, 0.5f, 0.5f, 0f, 0f, 1f,
            // back
            -0.5f, 0.5f, -0.5f, 0f, 0f, -1f,
            -0.5f, -0.5f, -0.5f, 0f, 0f, -1f,
            0.5f, -0.5f, -0.5f, 0f, 0f, -1f,
            -0.5f, 0.5f, -0.5f, 0f, 0f, -1f,
            0.5f, -0.5f, -0.5f, 0f, 0f, -1f,
            0.5f, 0.5f, -0.5f, 0f, 0f, -1f,
            // left
            -0.5f, 0.5f, -0.5f, -1f, 0f, 0f,
            -0.5f, -0.5f, -0.5f, -1f, 0f, 0f,
            -0.5f, -0.5f, 0.5f, -1f, 0f, 0f,
            -0.5f, 0.5f, -0.5f, -1f, 0f, 0f,
            -0.5f, -0.5f, 0.5f, -1f, 0f, 0f,
            -0.5f, 0.5f, 0.5f, -1f, 0f, 0f,
            // right
            0.5f, 0.5f, 0.5f, 1f, 0f, 0f,
            0.5f, -0.5f, 0.5f, 1f, 0f, 0f,
            0.5f, -0.5f, -0.5f, 1f, 0f, 0f,
            0.5f, 0.5f, 0.5f, 1f, 0f, 0f,
            0.5f, -0.5f, -0.5f, 1f, 0f, 0f,
            0.5f, 0.5f, -0.5f, 1f, 0f, 0f,
            // top
            -0.5f, 0.5f, -0.5f, 0f, 1f, 0f,
            -0.5f, 0.5f, 0.5f, 0f, 1f, 0f,
            0.5f, 0.5f, 0.5f, 0f, 1f, 0f,
            -0.5f, 0.5f, -0.5f, 0f, 1f, 0f,
            0.5f, 0.5f, 0.5f, 0f, 1f, 0f,
            0.5f, 0.5f, -0.5f, 0f, 1f, 0f,
            // bottom
            -0.5f, -0.5f, -0.5f, 0f, -1f, 0f,
            -0.5f, -0.5f, 0.5f, 0f, -1f, 0f,
            0.5f, -0.5f, 0.5f, 0f, -1f, 0f,
            -0.5f, -0.5f, -0.5f, 0f, -1f, 0f,
            0.5f, -0.5f, 0.5f, 0f, -1f, 0f,
            0.5f, -0.5f, -0.5f, 0f, -1f, 0f,
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

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 1, 1);

        drawCube();

        drawLight();
    }

    private void drawCube() {
        GLES20.glUseProgram(programHandle);

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

        vertexBuffer.position(VERTEX_COORD_START);
        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_COORD_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        vertexBuffer.position(VERTEX_NORMAL_START);
        int normalHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, PER_VERTEX_NORMAL_SIZE, GLES20.GL_FLOAT, true, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / PER_VERTEX_SIZE);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
    }

    private void drawLight() {
        GLES20.glUseProgram(lightPositionProgramHandle);

        int modelMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ModelMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0);

        int viewMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ViewMatrix");
        GLES20.glUniformMatrix4fv(viewMatrixHandle, 1, false, viewMatrix, 0);

        int projectionMatrixHandle = GLES20.glGetUniformLocation(lightPositionProgramHandle, "u_ProjectionMatrix");
        GLES20.glUniformMatrix4fv(projectionMatrixHandle, 1, false, projectionMatrix, 0);

        lightPositionBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(lightPositionProgramHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_LIGHT_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_LIGHT_VERTEX_STRIDE, lightPositionBuffer);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public void setAmbientStrength(float value) {
        ambientStrength = value;
    }

    public void setDiffuseStrength(float diffuseStrength) {
    }
}
