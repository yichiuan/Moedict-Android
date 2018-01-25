package com.yichiuan.moedict.common

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import java.nio.ByteBuffer

object TextUtil {

    fun obtainSpannedFrom(bytes: ByteBuffer): Spanned {
        val buf = bytes.asCharBuffer()

        val startTag = '`'
        val endTag = '~'

        var start = 0
        var end = buf.position() - 1

        val endPos = buf.limit()

        val builder = SpannableStringBuilder()

        for (i in buf.position() until endPos) {

            val current = buf.get(i)
            if (current == startTag) {
                start = i

                if (start != end + 1) {
                    builder.append(buf, end + 1, start)
                }
            }
            if (current == endTag) {
                end = i
                val wordLength = end - (start + 1)

                builder.append(buf, start + 1, end)

                builder.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val tv = widget as TextView
                        val s = tv.text as Spanned

                        val con = tv.text.subSequence(s.getSpanStart(this), s.getSpanEnd(this))
                        val conStr = con.toString()
                        Log.d("Span: ", conStr)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        // ds.setColor(ds.linkColor);
                        // ds.setUnderlineText(true);
                    }
                }, builder.length - wordLength, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        val lastWordsLength = endPos - (end + 1)
        if (lastWordsLength > 0) {
            builder.append(buf, end + 1, endPos)
        }

        return builder
    }

    fun obtainSpannedFrom(text: String): Spanned {

        val startTag = '`'
        val endTag = '~'

        var start = 0
        var end = -1

        val endPos = text.length

        val builder = SpannableStringBuilder()

        for (i in 0 until endPos) {

            val current = text[i]
            if (current == startTag) {
                start = i

                if (start != end + 1) {
                    builder.append(text, end + 1, start)
                }
            }
            if (current == endTag) {
                end = i
                val wordLength = end - (start + 1)

                builder.append(text, start + 1, end)

                builder.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val tv = widget as TextView
                        val s = tv.text as Spanned

                        val con = tv.text.subSequence(s.getSpanStart(this), s.getSpanEnd(this))
                        val conStr = con.toString()
                        Log.d("Span: ", conStr)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        // ds.setColor(ds.linkColor);
                        // ds.setUnderlineText(true);
                    }
                }, builder.length - wordLength, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        val lastWordsLength = endPos - (end + 1)
        if (lastWordsLength > 0) {
            builder.append(text, end + 1, endPos)
        }

        return builder
    }

    fun isChinese(codePoint: Int): Boolean {
        return (codePoint in 0x4E00..0x9FFF // CJK Unified Ideographs

                || codePoint in 0xF900..0xFAFF // CJK Compatibility Ideographs

                || codePoint in 0x3400..0x4DBF // CJK Unified Ideographs Extension A

                || codePoint in 0x20000..0x2A6DF // CJK Unified Ideographs Extension B

                || codePoint in 0x2A700..0x2B81F) // CJK Unified Ideographs Extension C and D
    }
}
