package com.yichiuan.moedict.common;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class TextUtil {

    public static Spanned obtainSpannedFrom(ByteBuffer bytes) {
        CharBuffer buf = bytes.asCharBuffer();

        final char startTag = '`';
        final char endTag = '~';

        int start = 0;
        int end = buf.position() - 1;

        final int endPos = buf.limit();

        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = buf.position(); i < endPos; ++i) {

            final char current = buf.get(i);
            if (current == startTag) {
                start = i;

                if (start != end + 1) {
                    builder.append(buf, end + 1, start);
                }
            }
            if (current == endTag) {
                end = i;
                int wordLength = end - (start + 1);

                builder.append(buf, start + 1, end);

                builder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        TextView tv = (TextView) widget;
                        Spanned s = (Spanned) tv.getText();
                        int start = s.getSpanStart(this);
                        int end = s.getSpanEnd(this);

                        CharSequence con = tv.getText().subSequence(start, end);
                        String conStr = con.toString();
                        Log.d("Span: ", conStr);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        // ds.setColor(ds.linkColor);
                        // ds.setUnderlineText(true);
                    }
                }, builder.length() - wordLength, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        int lastWordsLength = endPos - (end + 1);
        if (lastWordsLength > 0) {
            builder.append(buf, end + 1, endPos);
        }

        return builder;
    }

    public static Spanned obtainSpannedFrom(String text) {

        final char startTag = '`';
        final char endTag = '~';

        int start = 0;
        int end = -1;

        final int endPos = text.length();

        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 0; i < endPos; ++i) {

            final char current = text.charAt(i);
            if (current == startTag) {
                start = i;

                if (start != end + 1) {
                    builder.append(text, end + 1, start);
                }
            }
            if (current == endTag) {
                end = i;
                int wordLength = end - (start + 1);

                builder.append(text, start + 1, end);

                builder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        TextView tv = (TextView) widget;
                        Spanned s = (Spanned) tv.getText();
                        int start = s.getSpanStart(this);
                        int end = s.getSpanEnd(this);

                        CharSequence con = tv.getText().subSequence(start, end);
                        String conStr = con.toString();
                        Log.d("Span: ", conStr);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        // ds.setColor(ds.linkColor);
                        // ds.setUnderlineText(true);
                    }
                }, builder.length() - wordLength, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        int lastWordsLength = endPos - (end + 1);
        if (lastWordsLength > 0) {
            builder.append(text, end + 1, endPos);
        }

        return builder;
    }

    public static boolean isChinese(int codePoint) {
        return (codePoint >= 0x4E00 && codePoint <= 0x9FFF) // CJK Unified Ideographs
                || (codePoint >= 0xF900 && codePoint <= 0xFAFF) // CJK Compatibility Ideographs
                || (codePoint >= 0x3400 && codePoint <= 0x4DBF) // CJK Unified Ideographs Extension A
                || (codePoint >= 0x20000 && codePoint <= 0x2A6DF) // CJK Unified Ideographs Extension B
                || (codePoint >= 0x2A700 && codePoint <= 0x2B81F); // CJK Unified Ideographs Extension C and D
    }
}
