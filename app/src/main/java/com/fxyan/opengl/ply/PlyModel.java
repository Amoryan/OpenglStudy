package com.fxyan.opengl.ply;

import android.opengl.GLES20;

import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReaderFile;

import java.io.IOException;
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
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private final int PER_FACE_VERTEX_COUNT = 3;

    private float[] vertex;
    private int[] index;

    public PlyModel(PlyActivity context, String path) {
        PlyReaderFile reader = null;
        try {
            reader = new PlyReaderFile(context.getAssets().open(path));
            // 顶点
            ElementReader elementReader = reader.nextElementReader();
            vertex = new float[elementReader.getCount() * PER_VERTEX_SIZE];
            for (int i = 0; i < elementReader.getCount(); i++) {
                Element element = elementReader.readElement();
                vertex[i * PER_VERTEX_SIZE] = (float) element.getDouble("x");
                vertex[i * PER_VERTEX_SIZE + 1] = (float) element.getDouble("y");
                vertex[i * PER_VERTEX_SIZE + 2] = (float) element.getDouble("z");
            }
            elementReader.close();
            // 面
            elementReader = reader.nextElementReader();
            index = new int[elementReader.getCount() * PER_FACE_VERTEX_COUNT];
            for (int i = 0; i < elementReader.getCount(); i++) {
                Element element = elementReader.readElement();
                int[] vertex_indices = element.getIntList("vertex_indices");
                index[i * PER_FACE_VERTEX_COUNT] = vertex_indices[0];
                index[i * PER_FACE_VERTEX_COUNT + 1] = vertex_indices[1];
                index[i * PER_FACE_VERTEX_COUNT + 2] = vertex_indices[2];
            }
            elementReader.close();

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onDrawFrame(float[] mvpMatrix, int programHandle) {
        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        int aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, PER_VERTEX_SIZE, GLES20.GL_FLOAT, false, PER_VERTEX_STRIDE, vertexBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_INT, indexBuffer);
    }
}
