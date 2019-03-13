package com.fxyan.opengl.ply;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author fxYan
 */
public final class PlyModel {

    private final int PER_VERTEX_SIZE = 3;
    private final int PER_FLOAT_BYTES = 4;
    private final int PER_VERTEX_STRIDE = PER_VERTEX_SIZE * PER_FLOAT_BYTES;

    private String format;

    private int vertexCount;
    private int faceCount;

    public PlyModel(Context context, String assetsPath) {
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new InputStreamReader(context.getAssets().open(assetsPath)));
            String line;
            while ((line = bufr.readLine()) != null) {
                String[] split = line.split(" ");
                if (Tag.FORMAT.equals(split[0])) {
                    format = split[0];
                } else if (Tag.ELEMENT.equals(split[0])) {
                    if (Element.VERTEX.equals(split[1])) {
                        vertexCount = Integer.parseInt(split[2]);
                    } else if (Element.FACE.equals(split[1])) {
                        faceCount = Integer.parseInt(split[2]);
                    }
                }
            }
            Log.d("fxYan", String.format("vertex: %s, face: %s", vertexCount, faceCount));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufr != null) {
                try {
                    bufr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
