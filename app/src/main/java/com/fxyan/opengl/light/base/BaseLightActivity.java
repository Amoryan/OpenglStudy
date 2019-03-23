package com.fxyan.opengl.light.base;

import android.opengl.GLSurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fxyan.opengl.base.BaseActivity;
import com.fxyan.opengl.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fxYan
 */
public final class BaseLightActivity
        extends BaseActivity
        implements SeekBar.OnSeekBarChangeListener,
        GLSurfaceView.Renderer {

    private GLSurfaceView surfaceView;

    private TextView ambientValue;
    private TextView diffuseValue;
    private TextView specularValue;
    private TextView shininessValue;
    private TextView redValue;
    private TextView greenValue;
    private TextView blueValue;

    private BaseLight object;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_light;
    }

    @Override
    protected void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);

        ambientValue = findViewById(R.id.ambientValue);
        diffuseValue = findViewById(R.id.diffuseValue);
        specularValue = findViewById(R.id.specularValue);
        shininessValue = findViewById(R.id.shininessValue);
        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);
    }

    @Override
    protected void initData() {
        object = new BaseLight();
    }

    @Override
    protected void initEvents() {
        int[] ids = {
                R.id.ambientStrength,
                R.id.diffuseStrength,
                R.id.specularStrength,
                R.id.shininessStrength,
                R.id.redStrength,
                R.id.greenStrength,
                R.id.blueStrength,
        };
        int[] defProgress = {
                3, 7, 3, 5, 217, 142, 212,
        };
        for (int i = 0; i < ids.length; i++) {
            SeekBar seekBar = (SeekBar) findViewById(ids[i]);
            seekBar.setOnSeekBarChangeListener(this);
            seekBar.setProgress(defProgress[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        object.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        object.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        object.onDrawFrame();
    }
}
