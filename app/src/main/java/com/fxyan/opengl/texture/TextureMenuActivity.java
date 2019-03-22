package com.fxyan.opengl.texture;

import com.fxyan.opengl.BaseMenuActivity;
import com.fxyan.opengl.entity.ActivityMenu;
import com.fxyan.opengl.texture.feature.ClampToEdgeActivity;
import com.fxyan.opengl.texture.feature.MirroredRepeatActivity;
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
    }

}
