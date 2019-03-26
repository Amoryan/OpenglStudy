package com.fxyan.opengl.texture;

import com.fxyan.opengl.base.ActivityMenu;
import com.fxyan.opengl.base.BaseMenuActivity;
import com.fxyan.opengl.texture.cube.CubeTextureActivity;
import com.fxyan.opengl.texture.feature.ClampToEdgeActivity;
import com.fxyan.opengl.texture.feature.LinearActivity;
import com.fxyan.opengl.texture.feature.MirroredRepeatActivity;
import com.fxyan.opengl.texture.feature.NearestActivity;
import com.fxyan.opengl.texture.feature.RepeatActivity;

import java.util.List;

/**
 * @author fxYan
 */
public final class TextureMenuActivity
        extends BaseMenuActivity {

    @Override
    protected void genData(List<ActivityMenu> data) {
        data.add(new ActivityMenu(RepeatActivity.class, "GL_REPEAT"));
        data.add(new ActivityMenu(MirroredRepeatActivity.class, "GL_MIRRORED_REPEAT"));
        data.add(new ActivityMenu(ClampToEdgeActivity.class, "GL_CLAMP_TO_EDGE"));
        data.add(new ActivityMenu(NearestActivity.class, "GL_NEAREST"));
        data.add(new ActivityMenu(LinearActivity.class, "GL_LINEAR"));
        data.add(new ActivityMenu(CubeTextureActivity.class, "立方体贴图"));
    }

}
