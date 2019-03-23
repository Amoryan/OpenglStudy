package com.fxyan.opengl.base;

import android.app.Activity;

/**
 * @author fxYan
 */
public final class ActivityMenu {
    public Class<? extends Activity> clazz;
    public String menu;

    public ActivityMenu(Class<? extends Activity> clazz, String menu) {
        this.clazz = clazz;
        this.menu = menu;
    }
}
