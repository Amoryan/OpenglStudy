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
    private TextView specularValue;
    private TextView shininessValue;
    private TextView redValue;
    private TextView greenValue;
    private TextView blueValue;

    @Override
    protected void init() {
        setObject(Light.class);

        ambientValue = findViewById(R.id.ambientValue);
        diffuseValue = findViewById(R.id.diffuseValue);
        specularValue = findViewById(R.id.specularValue);
        shininessValue = findViewById(R.id.shininessValue);
        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);

        int[] ids = {
                R.id.ambientStrength,
                R.id.diffuseStrength,
                R.id.specularStrength,
                R.id.shininessStrength,
                R.id.redStrength,
                R.id.greenStrength,
                R.id.blueStrength,
        };
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

        switch (seekBar.getId()) {
            case R.id.ambientStrength: {
                float value = progress / 10f;
                object.setAmbientStrength(value);
                ambientValue.setText(String.valueOf(value));
            }
            break;
            case R.id.diffuseStrength: {
                float value = progress / 10f;
                object.setDiffuseStrength(value);
                diffuseValue.setText(String.valueOf(value));
            }
            break;
            case R.id.specularStrength: {
                float value = progress / 10f;
                object.setSpecularStrength(value);
                specularValue.setText(String.valueOf(value));
            }
            break;
            case R.id.shininessStrength: {
                int shininess = ((int) Math.pow(2, progress));
                object.setShininessStrength(shininess);
                shininessValue.setText(String.valueOf(shininess));
            }
            break;
            case R.id.redStrength: {
                float red = progress / 255f;
                object.setRed(red);
                redValue.setText(String.valueOf(progress));
            }
            break;
            case R.id.greenStrength: {
                float green = progress / 255f;
                object.setGreen(green);
                greenValue.setText(String.valueOf(progress));
            }
            break;
            case R.id.blueStrength: {
                float blue = progress / 255f;
                object.setBlue(blue);
                blueValue.setText(String.valueOf(progress));
            }
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
