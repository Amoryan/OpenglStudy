package com.fxyan.opengl.light;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fxyan.opengl.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class BaseLightActivity
        extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener,
        GLSurfaceView.Renderer {

    private TextView ambientValue;
    private TextView diffuseValue;
    private TextView specularValue;
    private TextView shininessValue;
    private TextView redValue;
    private TextView greenValue;
    private TextView blueValue;

    private Light light;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_light);

        GLSurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);

        light = new Light();

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.ambientStrength: {
                float value = progress / 10f;
                light.setAmbientStrength(value);
                ambientValue.setText(String.valueOf(value));
            }
            break;
            case R.id.diffuseStrength: {
                float value = progress / 10f;
                light.setDiffuseStrength(value);
                diffuseValue.setText(String.valueOf(value));
            }
            break;
            case R.id.specularStrength: {
                float value = progress / 10f;
                light.setSpecularStrength(value);
                specularValue.setText(String.valueOf(value));
            }
            break;
            case R.id.shininessStrength: {
                int shininess = ((int) Math.pow(2, progress));
                light.setShininessStrength(shininess);
                shininessValue.setText(String.valueOf(shininess));
            }
            break;
            case R.id.redStrength: {
                float red = progress / 255f;
                light.setRed(red);
                redValue.setText(String.valueOf(progress));
            }
            break;
            case R.id.greenStrength: {
                float green = progress / 255f;
                light.setGreen(green);
                greenValue.setText(String.valueOf(progress));
            }
            break;
            case R.id.blueStrength: {
                float blue = progress / 255f;
                light.setBlue(blue);
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

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        light.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        light.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        light.onDrawFrame();
    }
}
