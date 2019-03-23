package com.fxyan.opengl.geometry.line;

import android.opengl.GLES20;
import android.widget.RadioGroup;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class LineActivity
        extends OpenGLActivity {

    private Line object;

    @Override
    public int getLayoutId() {
        return R.layout.activity_line;
    }

    @Override
    protected IModel getModel() {
        object = new Line();
        return object;
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        ((RadioGroup) findViewById(R.id.group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.lines:
                        object.setLineType(GLES20.GL_LINES);
                        break;
                    case R.id.lineStride:
                        object.setLineType(GLES20.GL_LINE_STRIP);
                        break;
                    case R.id.lineLoop:
                        object.setLineType(GLES20.GL_LINE_LOOP);
                        break;
                    default:
                }
            }
        });
    }
}
