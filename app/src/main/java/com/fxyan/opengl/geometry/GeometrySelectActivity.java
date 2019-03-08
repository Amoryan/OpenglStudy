package com.fxyan.opengl.geometry;

import com.fxyan.opengl.SelectActivity;
import com.fxyan.opengl.entity.RendererMenu;
import com.fxyan.opengl.entity.geometry.Square;
import com.fxyan.opengl.entity.geometry.Triangle;

import java.util.List;

/**
 * @author fxYan
 */
public final class GeometrySelectActivity extends SelectActivity {

    @Override
    protected void genData(List<RendererMenu> data) {
        data.add(new RendererMenu("三角形", Triangle.class));
        data.add(new RendererMenu("正方形", Square.class));
    }
}
