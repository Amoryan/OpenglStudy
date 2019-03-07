package com.fxyan.opengl;

import android.content.Context;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author fxYan
 */
public final class ResourceUtils {

    public static String readRaw(Context context, @RawRes int res) {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(res)));
            String line;
            while ((line = bufr.readLine()) != null) {
                builder.append(line);
                builder.append(" ");
            }
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
        return builder.toString();
    }

}
