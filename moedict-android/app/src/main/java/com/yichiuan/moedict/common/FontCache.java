package com.yichiuan.moedict.common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.ArrayMap;

public class FontCache {

    private static ArrayMap<String, Typeface> fontCache = new ArrayMap<>();

    public static Typeface getTypeface(Context context, String fontname) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}