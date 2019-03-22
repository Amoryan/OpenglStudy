package com.fxyan.opengl.light.diffusetexture;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author fxYan
 */
public final class DiffuseTexture {

    private final int PER_FLOAT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private final int PER_NORMAL_SIZE = 3;
    private final int PER_NORMAL_STRIDE = PER_NORMAL_SIZE * PER_FLOAT_BYTES;

    private final int PER_LIGHT_POS_SIZE = 3;
    private final int PER_LIGHT_POS_STRIDE = PER_LIGHT_POS_SIZE * PER_FLOAT_BYTES;

    // 环境光强度
    private float ambientStrength;
    // 漫反射强度
    private float diffuseStrength;
    // 镜面反射强度(物体粗糙度)
    private float specularStrength;
    // 反光度
    private float shininessStrength;

    private float red;
    private float green;
    private float blue;

    private FloatBuffer vertexBuffer;
    private float[] vertex = {
            // front
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            // back
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            // left
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            // right
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            // top
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            // bottom
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
    };

    private FloatBuffer normalBuffer;
    private float[] normal = {
            // front
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            // back
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            // left
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            // right
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            // top
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            // bottom
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
    };

    private FloatBuffer lightPosBuffer;
    private float[] lightModelMatrix = new float[16];
    private float[] lightPosInModelSpace = {0f, 0f, 1f, 1f};
    private float[] lightPosInWorldSpace = new float[4];
    private float[] lightPosInEyeSpace = new float[4];
    private float[] lightColor = {1f, 1f, 1f, 1f};

    private float[] cameraInWorldSpace = {0f, 0f, 3f};

    private int programHandle;
    private int lightPosProgramHandle;

    protected float[] mvpMatrix = new float[16];
    protected float[] modelMatrix = new float[16];
    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];

    public DiffuseTexture() {
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

        lightPosBuffer = ByteBuffer.allocateDirect(lightPosInModelSpace.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(lightPosInModelSpace);
        lightPosBuffer.position(0);
    }

    public void onSurfaceCreated() {
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        programHandle = GLESUtils.createAndLinkProgram("light/light.vert", "light/light.frag");
        lightPosProgramHandle = GLESUtils.createAndLinkProgram("light/lightpos.vert", "light/lightpos.frag");
    }

    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix.setLookAtM(viewMatrix, 0, cameraInWorldSpace[0], cameraInWorldSpace[1], cameraInWorldSpace[2], 0, 0, -5f, 0, 1, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        drawCube();

        drawLight();
    }

    private void drawCube() {
        GLES20.glUseProgram(programHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 1, 0);

        int modelMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_ModelMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        int mvMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVMatrix");
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, mvpMatrix, 0);

        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int colorHandle = GLES20.glGetUniformLocation(programHandle, "u_Color");
        GLES20.glUniform3f(colorHandle, red, green, blue);

        int lightColorHandle = GLES20.glGetUniformLocation(programHandle, "u_LightColor");
        GLES20.glUniform3f(lightColorHandle, lightColor[0], lightColor[1], lightColor[2]);

        Matrix.setIdentityM(lightModelMatrix, 0);
        Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0);
        int lightInWorldSpaceHandle = GLES20.glGetUniformLocation(programHandle, "u_LightInWorldSpace");
        GLES20.glUniform3f(lightInWorldSpaceHandle, lightPosInWorldSpace[0], lightPosInWorldSpace[1], lightPosInWorldSpace[2]);

        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0);
        int lightInEyeSpaceHandle = GLES20.glGetUniformLocation(programHandle, "u_LightInEyeSpace");
        GLES20.glUniform3f(lightInEyeSpaceHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2]);

        int cameraInWorldSpaceHandle = GLES20.glGetUniformLocation(programHandle, "u_CameraInWorldSpace");
        GLES20.glUniform3f(cameraInWorldSpaceHandle, cameraInWorldSpace[0], cameraInWorldSpace[1], cameraInWorldSpace[2]);

        int ambientStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_AmbientStrength");
        GLES20.glUniform1f(ambientStrengthHandle, ambientStrength);

        int diffuseStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_DiffuseStrength");
        GLES20.glUniform1f(diffuseStrengthHandle, diffuseStrength);

        int specularStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_SpecularStrength");
        GLES20.glUniform1f(specularStrengthHandle, specularStrength);

        int shininessStrengthHandle = GLES20.glGetUniformLocation(programHandle, "u_ShininessStrength");
        GLES20.glUniform1f(shininessStrengthHandle, shininessStrength);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        normalBuffer.position(0);
        int normalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, PER_NORMAL_SIZE, GLES20.GL_FLOAT, true, PER_NORMAL_STRIDE, normalBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length / PER_VERTEX_SIZE);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
    }

    private void drawLight() {
        GLES20.glUseProgram(lightPosProgramHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(lightPosProgramHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        lightPosBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(lightPosProgramHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_LIGHT_POS_SIZE, GLES20.GL_FLOAT, false, PER_LIGHT_POS_STRIDE, lightPosBuffer);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public void setAmbientStrength(float value) {
        ambientStrength = value;
    }

    public void setDiffuseStrength(float value) {
        diffuseStrength = value;
    }

    public void setSpecularStrength(float value) {
        this.specularStrength = value;
    }

    public void setShininessStrength(int shininess) {
        this.shininessStrength = shininess;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }
}
