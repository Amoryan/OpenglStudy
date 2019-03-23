package com.fxyan.opengl.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author fxYan
 */
public abstract class BaseActivity
        extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        context = this;

        initViews();

        initData();

        initEvents();
    }

    @LayoutRes
    public abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract void initEvents();

}
