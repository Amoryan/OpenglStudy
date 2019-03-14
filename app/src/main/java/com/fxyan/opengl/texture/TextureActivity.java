package com.fxyan.opengl.texture;

import com.fxyan.opengl.OpenGLActivity;
import com.fxyan.opengl.R;

/**
 * @author fxYan
 */
public final class TextureActivity extends OpenGLActivity {

    @Override
    protected void init() {
        setObject(Texture2D.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_texture;
    }

}
