package com.fxyan.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class LessonOneActivity extends AppCompatActivity
        implements GLSurfaceView.Renderer {

    public static final String U_MVPMATRIX = "u_MVPMatrix";
    public static final String A_POSITION = "a_Position";
    public static final String U_COLOR = "u_Color";

    private GLSurfaceView glview;
    // 风向坐标(顺时针坐标)
    private float[] vertexs = new float[]{
            1, -1, 0,

            -1, -1, 0,

            -1, 1, 0,

            -1, 1, 0,

            1, 1, 0,

            1, -1, 0
    };
    private float[] color = new float[]{
            0f, 0f, 0f, 1f
    };
    private FloatBuffer buffer;

    private float[] mvpMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int perVertexStride = 3 * 4;// 每个顶点占用的字节数
    private int offsetOf_a_Position = 0;//a_Position属性在每个顶点中的起始偏移量
    private int offsetOf_a_Color = 3;//a_Color属性在每个顶点中的起始偏移量

    private int locationOf_u_MVPMatrix;
    private int locationOf_a_Position;
    private int locationOf_u_Color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glview = findViewById(R.id.glview);
        glview.setEGLContextClientVersion(2);
        glview.setRenderer(this);

        buffer = ByteBuffer.allocateDirect(vertexs.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexs);
        buffer.position(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glview.onPause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置背景色
        GLES20.glClearColor(1f, 1F, 1F, 1f);

        /**
         * 设置眼睛的位置
         * eyeZ的值直接影响到正交矩阵的near值，near定义的是物体的背面，如果near > eyeZ，说明物体的背面都在眼睛的后面
         * 如果这个时候，眼睛的朝向又是z轴的负方向，那么肯定是看不到眼睛后面的物体的
         */
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 3f;

        /**
         * 设置眼睛所看的方向，这里设置的是看z轴的负方向
         */
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        /**
         * 设置头的朝向，这里头是朝y轴正方向
         * 头如果顺时针旋转90°，那么所看到的图像投影到手机屏幕上应该是逆时针旋转90°
         * 正常头是朝着y轴正方向，看到的图像是正常的
         */
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        String vertexShaderSource = ResourceUtils.readRaw(this, R.raw.vertexshader1);
        int vertexShaderObjectId = GL.createShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);

        String fragmentShaderSource = ResourceUtils.readRaw(this, R.raw.fragmentshader1);
        int fragmentShaderObjectId = GL.createShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        int programObjectId = GL.linkProgram(new String[]{"a_Position"}, vertexShaderObjectId, fragmentShaderObjectId);

        GLES20.glUseProgram(programObjectId);

        locationOf_u_MVPMatrix = GLES20.glGetUniformLocation(programObjectId, U_MVPMATRIX);
        locationOf_a_Position = GLES20.glGetAttribLocation(programObjectId, A_POSITION);
        locationOf_u_Color = GLES20.glGetUniformLocation(programObjectId, U_COLOR);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // 以上下作为[-1,1]的区间，那么相对于opengl的坐标系，左右的范围就是[-width/height, width/height]
        float ratio = ((float) width) / height;
        float left = -ratio;
        float right = ratio;
        float top = 1;
        float bottom = -1;
        float near = 1f;
        float far = 10f;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(locationOf_u_MVPMatrix, 0, false, mvpMatrix, 0);

        buffer.position(offsetOf_a_Position);
        GLES20.glVertexAttribPointer(locationOf_a_Position, 3, GLES20.GL_FLOAT, false, perVertexStride, buffer);
        // 允许vertex读取gpu的数据，必须加，不然显示不出来
        GLES20.glEnableVertexAttribArray(locationOf_a_Position);

        GLES20.glUniform4fv(locationOf_u_Color, 0, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexs.length / 3 * 3);
    }

}
