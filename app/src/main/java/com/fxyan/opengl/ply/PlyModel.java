package com.fxyan.opengl.ply;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.fxyan.opengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class PlyModel {

    private final int PER_FLOAT_BYTES = 4;
    private final int PER_INT_BYTES = 4;

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_VERTEX_COORD_SIZE = 3;
    private final int PER_VERTEX_NORMAL_SIZE = 3;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private float[] vertex;
    private int[] index;

    //    private float[] color = {220f / 255, 220f / 255, 245f / 255, 1f};// 白钻
//    private float[] color = {220f / 255, 220f / 255, 245f / 255, 1f};
    private float[] color = {255f / 255, 220f / 255, 0, 1f};// 黄钻

    private Context context;

    private float[] lightModelMatrix = new float[16];
    private float[] lightInModelSpace = {0f, 0f, 20f, 1f};
    private float[] lightInWorldSpace = new float[4];
    private float[] lightInEyeSpace = new float[4];

    public PlyModel(Context _context, float[] _vertex, int[] _index) {
        this.context = _context;

        this.vertex = _vertex;
        this.index = _index;

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

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.zuanshi);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onDrawFrame(float[] viewMatrix, float[] mvMatrix, float[] mvpMatrix, int programHandle) {
        int mvMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVMatrix");
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, mvMatrix, 0);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        Matrix.setIdentityM(lightModelMatrix, 0);
        Matrix.multiplyMV(lightInWorldSpace, 0, lightModelMatrix, 0, lightInModelSpace, 0);
        Matrix.multiplyMV(lightInEyeSpace, 0, viewMatrix, 0, lightInWorldSpace, 0);
        int lightInEyeSpaceHandle = GLES20.glGetUniformLocation(programHandle, "u_LightInEyeSpace");
        GLES20.glUniform3f(lightInEyeSpaceHandle, lightInEyeSpace[0], lightInEyeSpace[1], lightInEyeSpace[2]);

        int colorHandle = GLES20.glGetUniformLocation(programHandle, "u_Color");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_COORD_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

//        int normalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
//        GLES20.glEnableVertexAttribArray(normalHandle);
//        GLES20.glVertexAttribPointer(normalHandle, PER_VERTEX_NORMAL_SIZE, GLES20.GL_FLOAT, true, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
//        GLES20.glDisableVertexAttribArray(normalHandle);
    }
}
