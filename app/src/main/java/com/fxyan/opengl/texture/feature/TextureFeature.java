package com.fxyan.opengl.texture.feature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.fxyan.opengl.base.ModelImpl;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author fxYan
 */
public final class TextureFeature
        extends ModelImpl {

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
            2, 3, 0
    };
    private float[] texCoord = {
            2f, -1f,
            2f, 2f,
            -1f, 2f,
            -1f, -1f,
    };

    private int programHandle;
    private Context context;

    private int resId;
    private int wrapMode;
    private int minFilter;
    private int magFilter;

    public TextureFeature(Context context, int resId, int wrapMode, int minFilter, int magFilter) {
        this.context = context;
        this.wrapMode = wrapMode;
        this.resId = resId;
        this.minFilter = minFilter;
        this.magFilter = magFilter;

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

    public void onSurfaceCreated() {
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1f);

        programHandle = GLESUtils.createAndLinkProgram("texture/feature/feature.vert", "texture/feature/feature.frag");

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrapMode);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapMode);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    public void onSurfaceChanged(int width, int height) {
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1f, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 10);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);
    }

    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

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
