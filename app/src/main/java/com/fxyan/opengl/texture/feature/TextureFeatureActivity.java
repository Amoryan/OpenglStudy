package com.fxyan.opengl.texture.feature;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public abstract class TextureFeatureActivity
        extends OpenGLActivity {

    @Override
    protected IModel getModel() {
        return new TextureFeature(this, getWrapMode());
    }

    protected abstract int getWrapMode();

}
