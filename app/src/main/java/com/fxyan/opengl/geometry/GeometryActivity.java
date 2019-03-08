package com.fxyan.opengl.geometry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fxyan.opengl.OpenGLActivity;
import com.fxyan.opengl.R;
import com.fxyan.opengl.entity.RendererMenu;
import com.fxyan.opengl.entity.geometry.Triangle;

/**
 * @author fxYan
 */
public final class GeometryActivity extends OpenGLActivity {

    @Override
    protected void init() {
        setObject(Triangle.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opengl;
    }

    public void geometry(View view) {
        startActivityForResult(new Intent(this, GeometrySelectActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                RendererMenu menu = (RendererMenu) bundle.getSerializable("menu");

                if (menu == null) return;

                setObject(menu.clazz);
            }
        }
    }
}
