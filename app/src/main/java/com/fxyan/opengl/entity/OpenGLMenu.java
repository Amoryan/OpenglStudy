package com.fxyan.opengl.entity;

import android.app.Activity;

/**
 * @author fxYan
 */
public final class OpenGLMenu {
    public Class<? extends Activity> clazz;
    public String menu;

    public OpenGLMenu(Class<? extends Activity> clazz, String menu) {
        this.clazz = clazz;
        this.menu = menu;
    }
}
