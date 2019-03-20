package com.fxyan.opengl.ply;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class ZSPlyModel extends PlyModel {

    public ZSPlyModel(Context _context, float[] _vertex, float[] _normal, int[] _index) {
        super(_context, _vertex, _normal, _index);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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

        int textureHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        GLES20.glUniform1i(textureHandle, 1);

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        int normalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, PER_NORMAL_SIZE, GLES20.GL_FLOAT, true, PER_NORMAL_STRIDE, normalBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
    }
}
