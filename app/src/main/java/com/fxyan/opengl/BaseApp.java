package com.fxyan.opengl;

import android.app.Application;
import android.content.Context;

/**
 * @author fxYan
 */
public final class BaseApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

}
