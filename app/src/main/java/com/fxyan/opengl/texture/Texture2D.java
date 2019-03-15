package com.fxyan.opengl.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.fxyan.opengl.R;
import com.fxyan.opengl.utils.GLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author fxYan
 * 2D纹理贴图
 */
public final class Texture2D
        implements ITexture {

    private final int PER_FLOAT_BYTES = 4;
    private final int PER_INT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 2;
    private final int PER_VERTEX_STRIDE = PER_FLOAT_BYTES * PER_VERTEX_SIZE;

    private final int PER_TEX_COORD_SIZE = 2;
    private final int PER_TEX_COORD_STRIDE = PER_FLOAT_BYTES * PER_TEX_COORD_SIZE;

    private final int PER_COLOR_SIZE = 4;
    private final int PER_COLOR_STRIDE = PER_FLOAT_BYTES * PER_COLOR_SIZE;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private FloatBuffer colorBuffer;
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
    private float[] color = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f
    };
    private float[] texCoord = {
            0f, 0f,
            0f, 2f,
            2f, 2f,
            2f, 0f,
    };
    private int[] textures;

    private float[] mvpMatrix = new float[16];
    private float[] mvMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int programHandle;
    private Context context;

    private int sMode;
    private int tMode;
    private int minFilter;
    private int magFilter;

    public Texture2D(Context _context, int _sMode, int _tMode, int _minFilter, int _magFilter) {
        this.context = _context;
        this.sMode = _sMode;
        this.tMode = _tMode;
        this.minFilter = _minFilter;
        this.magFilter = _magFilter;

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

        colorBuffer = ByteBuffer.allocateDirect(color.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(color);
        colorBuffer.position(0);

        texCoordBuffer = ByteBuffer.allocateDirect(texCoord.length * PER_FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texCoord);
        texCoordBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated() {
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1f);

        programHandle = GLESUtils.createAndLinkProgram("texture/texture2d.vert", "texture/texture2d.frag");

        /**
         * 第一个参数是生成纹理的数量
         * 第二个参数是生成的纹理id存放的数组
         * 第三个参数是偏移量
         */
        textures = new int[2];
        GLES20.glGenTextures(2, textures, 0);

        // 绑定纹理，默认GL_TEXTURE0的纹理单元是激活的
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        // 设置缩小过滤
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        // 设置放大过滤
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
        // 设置环绕方向S
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, sMode);
        // 设置环绕方向T
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, tMode);
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.texture2d);
        /**
         * 第二个参数表示多级远近纹理的级别
         */
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap1, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, sMode);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, tMode);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.kb);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap2, 0);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3f, 0, 0, -5f, 0, 1f, 0);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 10);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);
    }

    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        int colorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, PER_COLOR_SIZE, GLES20.GL_FLOAT, false, PER_TEX_COORD_STRIDE, colorBuffer);

        int texCoordHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoord");
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, PER_TEX_COORD_SIZE, GLES20.GL_FLOAT, false, PER_TEX_COORD_STRIDE, texCoordBuffer);

        // u_Texture1绑定第一个纹理
        int texture1Handle = GLES20.glGetUniformLocation(programHandle, "u_Texture1");
        GLES20.glUniform1i(texture1Handle, 0);
        // u_Texture2绑定第二个纹理
        int texture2Handle = GLES20.glGetUniformLocation(programHandle, "u_Texture2");
        GLES20.glUniform1i(texture2Handle, 1);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }

}
