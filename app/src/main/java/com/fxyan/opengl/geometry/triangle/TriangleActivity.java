package com.fxyan.opengl.geometry.triangle;

import android.opengl.GLES20;
import android.widget.RadioGroup;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class TriangleActivity
        extends OpenGLActivity
        implements RadioGroup.OnCheckedChangeListener {

    private Triangle object;

    @Override
    public int getLayoutId() {
        return R.layout.activity_triangle;
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        ((RadioGroup) findViewById(R.id.group)).setOnCheckedChangeListener(this);
    }

    @Override
    protected IModel getModel() {
        object = new Triangle();
        return object;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.triangles:
                object.setMode(GLES20.GL_TRIANGLES);
                break;
            case R.id.triangleStrip:
                object.setMode(GLES20.GL_TRIANGLE_STRIP);
                break;
            case R.id.triangleFan:
                object.setMode(GLES20.GL_TRIANGLE_FAN);
                break;
            default:
        }
    }
}
