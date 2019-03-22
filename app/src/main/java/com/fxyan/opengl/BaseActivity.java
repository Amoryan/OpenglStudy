package com.fxyan.opengl;

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

        Bundle bundle = savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;
        initParams(bundle);

        initViews();

        initData();

        initEvents();
    }

    @LayoutRes
    public abstract int getLayoutId();

    protected abstract void initParams(Bundle bundle);

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract void initEvents();

}
