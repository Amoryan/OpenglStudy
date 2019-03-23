package com.fxyan.opengl.geometry;

import com.fxyan.opengl.base.BaseMenuActivity;
import com.fxyan.opengl.base.ActivityMenu;
import com.fxyan.opengl.geometry.cube.CubeActivity;
import com.fxyan.opengl.geometry.square.SquareActivity;
import com.fxyan.opengl.geometry.triangle.TriangleActivity;

import java.util.List;

/**
 * @author fxYan
 */
public final class GeometryMenuActivity
        extends BaseMenuActivity {

    @Override
    protected void genData(List<ActivityMenu> data) {
        data.add(new ActivityMenu(TriangleActivity.class, "三角形"));
        data.add(new ActivityMenu(SquareActivity.class, "正方形"));
        data.add(new ActivityMenu(CubeActivity.class, "立方体"));
    }
}
