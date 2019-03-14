package com.fxyan.opengl.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.fxyan.opengl.R;
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
 * 2D纹理贴图
 */
public final class Texture2D extends ObjectImpl {

    private final int PER_FLOAT_BYTES = 4;
    private final int PER_INT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 2;
    private final int PER_VERTEX_STRIDE = PER_FLOAT_BYTES * PER_VERTEX_SIZE;

    private final int PER_TEX_COORD_SIZE = 2;
    private final int PER_TEX_COORD_STRIDE = PER_FLOAT_BYTES * PER_TEX_COORD_SIZE;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private FloatBuffer texCoordBuffer;

    private float[] vertex = {
            // left top
            -0.5f, 0.5f,
            // left bottom
            -0.5f, -0.5f,
            // right bottom
            0.5f, -0.5f,
            // right top
            0.5f, 0.5f,
    };
    private int[] index = {
            0, 1, 2,
            0, 2, 3
    };
    private float[] texCoord = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
    };

    private int programHandle;
    private Context context;

    public Texture2D(Context context) {
        this.context = context;
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

        texCoordBuffer = ByteBuffer.allocateDirect(texCoord.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texCoord);
        texCoordBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        programHandle = GLESUtils.createAndLinkProgram("texture/texture2d.vert", "texture/texture2d.frag");

        // 生成纹理
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        // 绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        // 设置缩小过滤
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        // 设置放大过滤
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        // 设置环绕方向S
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        // 设置环绕方向T
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.texture2d);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
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

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        int texCoordHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoord");
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, PER_TEX_COORD_SIZE, GLES20.GL_FLOAT, false, PER_TEX_COORD_STRIDE, texCoordBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }
}
