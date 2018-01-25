package com.yichiuan.moedict.common

import android.content.Context
import android.graphics.Typeface
import android.support.v4.util.ArrayMap

object FontCache {

    private val fontCache = ArrayMap<String, Typeface>()

    fun getTypeface(context: Context, fontName: String): Typeface? {
        var typeface: Typeface? = fontCache[fontName]

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                return null
            }

            fontCache[fontName] = typeface
        }

        return typeface
    }
}