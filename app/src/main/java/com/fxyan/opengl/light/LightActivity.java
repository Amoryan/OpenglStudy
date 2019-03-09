package com.fxyan.opengl.light;

import android.widget.CompoundButton;
import android.widget.Switch;

import com.fxyan.opengl.OpenGLActivity;
import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class LightActivity
        extends OpenGLActivity
        implements CompoundButton.OnCheckedChangeListener {

    private boolean ambient;
    private boolean diffuse;
    private boolean specular;

    @Override
    protected void init() {
        setObject(Light.class);
        int[] ids = {R.id.ambient, R.id.diffuse, R.id.specular};
        for (int id : ids) {
            ((Switch) findViewById(id)).setOnCheckedChangeListener(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_light;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ambient:
                ((Light) getObject()).setAmbient(isChecked);
                break;
            case R.id.diffuse:
                ((Light) getObject()).setDiffuse(isChecked);
                break;
            case R.id.specular:
                ((Light) getObject()).setSpecular(isChecked);
                break;
            default:
        }
    }
}
