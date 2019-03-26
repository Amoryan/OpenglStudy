package com.fxyan.opengl.light;

import com.fxyan.opengl.base.ActivityMenu;
import com.fxyan.opengl.base.BaseMenuActivity;
import com.fxyan.opengl.light.base.BaseLightInFragmentShaderActivity;
import com.fxyan.opengl.light.base.BaseLightInVertexShaderActivity;
import com.fxyan.opengl.light.texture.LightTextureActivity;

import java.util.List;

/**
 * @author fxYan
 */
public final class LightMenuActivity
        extends BaseMenuActivity {

    @Override
    protected void genData(List<ActivityMenu> data) {
        data.add(new ActivityMenu(BaseLightInVertexShaderActivity.class, "基础光照(Vertex Shader)"));
        data.add(new ActivityMenu(BaseLightInFragmentShaderActivity.class, "基础光照(Fragment Shader)"));
        data.add(new ActivityMenu(LightTextureActivity.class, "光照贴图"));
    }

}