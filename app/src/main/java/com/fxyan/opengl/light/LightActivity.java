package com.fxyan.opengl.light;

import android.widget.SeekBar;
import android.widget.TextView;

import com.fxyan.opengl.OpenGLActivity;
import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class LightActivity
        extends OpenGLActivity
        implements SeekBar.OnSeekBarChangeListener {

    private TextView ambientValue;
    private TextView diffuseValue;

    @Override
    protected void init() {
        setObject(Light.class);

        ambientValue = findViewById(R.id.ambientValue);
        diffuseValue = findViewById(R.id.diffuseValue);

        int[] ids = {R.id.ambientStrength, R.id.diffuseStrength};
        for (int id : ids) {
            ((SeekBar) findViewById(id)).setOnSeekBarChangeListener(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_light;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Light object = (Light) getObject();

        if (object == null) return;

        float value = progress / 10f;

        switch (seekBar.getId()) {
            case R.id.ambientStrength:
                object.setAmbientStrength(value);
                ambientValue.setText(String.valueOf(value));
                break;
            case R.id.diffuseStrength:
                object.setDiffuseStrength(value);
                diffuseValue.setText(String.valueOf(value));
                break;
            default:
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
