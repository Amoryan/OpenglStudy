package com.fxyan.opengl;

import com.fxyan.opengl.base.ActivityMenu;
import com.fxyan.opengl.base.BaseMenuActivity;
import com.fxyan.opengl.geometry.GeometryMenuActivity;
import com.fxyan.opengl.light.LightMenuActivity;
import com.fxyan.opengl.ply.PlyActivity;
import com.fxyan.opengl.texture.TextureMenuActivity;

import java.util.List;

public class MainActivity
        extends BaseMenuActivity {

    @Override
    protected void genData(List<ActivityMenu> data) {
        data.add(new ActivityMenu(GeometryMenuActivity.class, "几何图形"));
        data.add(new ActivityMenu(TextureMenuActivity.class, "纹理"));
        data.add(new ActivityMenu(LightMenuActivity.class, "光照"));
        data.add(new ActivityMenu(PlyActivity.class, "Ply模型"));
    }
}
