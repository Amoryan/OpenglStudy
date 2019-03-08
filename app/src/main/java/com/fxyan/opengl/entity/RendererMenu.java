package com.fxyan.opengl.entity;

import java.io.Serializable;

/**
 * @author fxYan
 */
public final class RendererMenu implements Serializable {
    public String name;
    public Class<? extends IObject> clazz;

    public RendererMenu(String name, Class<? extends IObject> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
