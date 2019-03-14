package com.fxyan.opengl.texture;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fxyan.opengl.entity.geometry.ObjectImpl;
import com.fxyan.opengl.utils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 * 2D纹理贴图
 */
public final class Texture2D extends ObjectImpl {

    private final int PER_FLOAT_BYTES = 4;
    private final int PER_INT_BYTES = 4;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    private float[] vertex = {
            // left top
            -0.5f, 0.5f, 0f,
            // left bottom
            -0.5f, -0.5f, 0f,
            // right bottom
            0.5f, -0.5f, 0f,
            // right top
            0.5f, 0.5f, 0f
    };
    private int[] index = {
            0, 1, 2,
            0, 2, 3
    };

    private int programHandle;

    public Texture2D() {
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        programHandle = GLUtils.createAndLinkProgram("texture/texture2d.vert", "texture/texture2d.frag");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1f, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        
    }
}
