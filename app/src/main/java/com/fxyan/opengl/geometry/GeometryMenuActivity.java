package com.fxyan.opengl.geometry;

import com.fxyan.opengl.base.ActivityMenu;
import com.fxyan.opengl.base.BaseMenuActivity;
import com.fxyan.opengl.geometry.circle.CircleActivity;
import com.fxyan.opengl.geometry.cube.CubeActivity;
import com.fxyan.opengl.geometry.cylindrical.CylindricalActivity;
import com.fxyan.opengl.geometry.line.LineActivity;
import com.fxyan.opengl.geometry.point.PointActivity;
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
        data.add(new ActivityMenu(PointActivity.class, "点"));
        data.add(new ActivityMenu(LineActivity.class, "线"));
        data.add(new ActivityMenu(TriangleActivity.class, "三角形"));
        data.add(new ActivityMenu(SquareActivity.class, "正方形"));
        data.add(new ActivityMenu(CircleActivity.class, "圆"));
        data.add(new ActivityMenu(CylindricalActivity.class, "圆柱"));
        data.add(new ActivityMenu(CubeActivity.class, "立方体"));
    }
}
