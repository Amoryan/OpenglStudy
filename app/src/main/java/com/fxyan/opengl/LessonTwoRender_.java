package com.fxyan.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class LessonTwoRender_ implements GLSurfaceView.Renderer {

    private Context context;

    public LessonTwoRender_(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(cubeVertex.length * perFloatByte)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(cubeVertex);
        vertexBuffer.position(0);

        vertexColorBuffer = ByteBuffer.allocateDirect(faceColor.length * perFloatByte)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(faceColor);
        vertexBuffer.position(0);
    }

    // open gl 默认是逆风向
    private float[] cubeVertex = new float[]{

            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
    };

    private float[] faceColor = new float[]{
            // front red
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            //  right green
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            // back blue
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            //left black
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            0f, 0f, 0f, 1f,
            // top
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            // bottom
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
    };

    private final int perFloatByte = 4;
    private FloatBuffer vertexBuffer;
    private FloatBuffer vertexColorBuffer;
    private final int vertexStride = 3 * 4;
    private final int faceStride = 4 * 4;

    private float[] mvpMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int locationOf_U_MVPMatrix;
    private int locationOf_a_Position;
    private int locationOf_a_Color;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // 开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 5f;

        float centerX = 0;
        float centerY = 0;
        float centerZ = -5f;

        float upX = 0f;
        float upY = 1f;
        float upZ = 0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

        int vertexObjectId = GL.createShader(GLES20.GL_VERTEX_SHADER, ResourceUtils.readRaw(context, R.raw.vertexlesson2));
        int fragmentObjectId = GL.createShader(GLES20.GL_FRAGMENT_SHADER, ResourceUtils.readRaw(context, R.raw.fragmentlesson2));
        int programObjectId = GL.linkProgram(new String[]{"a_Position", "a_Color"}, vertexObjectId, fragmentObjectId);

        GLES20.glUseProgram(programObjectId);

        locationOf_U_MVPMatrix = GLES20.glGetUniformLocation(programObjectId, "u_MVPMatrix");
        locationOf_a_Position = GLES20.glGetAttribLocation(programObjectId, "a_Position");
        locationOf_a_Color = GLES20.glGetAttribLocation(programObjectId, "a_Color");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        float top = 1f;
        float bottom = -1f;
        float left = -ratio;
        float right = ratio;
        float near = 1f;
        float far = 10f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(locationOf_a_Position, 3, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(locationOf_a_Position);

        vertexColorBuffer.position(0);
        GLES20.glVertexAttribPointer(locationOf_a_Color, 4, GLES20.GL_FLOAT, false, faceStride, vertexColorBuffer);
        GLES20.glEnableVertexAttribArray(locationOf_a_Color);

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.setIdentityM(modelMatrix, 0);
        // 旋转轴心,x,y,z正方向
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(locationOf_U_MVPMatrix, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeVertex.length / 3 * 3);
    }
}
