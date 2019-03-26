package com.fxyan.opengl.light.base;

import android.opengl.GLSurfaceView;
import android.widget.SeekBar;

import com.fxyan.opengl.base.IModel;

/**
 * @author fxYan
 */
public final class BaseLightInFragmentShaderActivity
        extends BaseLightInVertexShaderActivity
        implements SeekBar.OnSeekBarChangeListener,
        GLSurfaceView.Renderer {

    @Override
    protected IModel getModel() {
        object = new BaseLight(BaseLight.CALCULATE_IN_FRAGMENT_SHADER);
        return object;
    }

}
