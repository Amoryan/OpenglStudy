package com.fxyan.opengl.texture.feature;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public abstract class TextureFeatureActivity
        extends OpenGLActivity {

    @Override
    protected IModel getModel() {
        return new TextureFeature(this, getResId(), getWrapMode(), getMinFilter(), getMagFilter());
    }

    protected int getResId() {
        return R.mipmap.ic_huaji;
    }

    protected abstract int getWrapMode();

    protected abstract int getMinFilter();

    protected abstract int getMagFilter();

}
