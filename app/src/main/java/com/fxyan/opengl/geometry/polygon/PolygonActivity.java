package com.fxyan.opengl.geometry.polygon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fxyan.opengl.base.IModel;
import com.fxyan.opengl.base.OpenGLActivity;

/**
 * @author fxYan
 */
public final class PolygonActivity
        extends OpenGLActivity {

    private static final String EDGE_NUM = "edgeNum";

    private int edgeNum;

    public static void open(Context context, int edgeNum) {
        Intent intent = new Intent(context, PolygonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EDGE_NUM, edgeNum);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        edgeNum = getIntent().getExtras().getInt(EDGE_NUM, 3);
        super.initData();
    }

    @Override
    protected IModel getModel() {
        return new Polygon(edgeNum);
    }
}
