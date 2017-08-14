package com.yichiuan.moedict.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;


public class PinyinSpan extends ReplacementSpan {

    private static final char neutralTone = '˙';
    private static final char secondTone = 'ˊ';
    private static final char thirdTone = 'ˇ';
    private static final char fourthTone = 'ˋ';

    private static final float BOPOMOFO_FONT_RATIO = 9f / 30f;
    private static final float TONE_FONT_RATIO = 5f / 9f;

    private TextPaint pinyinPaint;

    private TextPaint tonePaint;

    private String pinyin;

    private float wordWidth;

    public PinyinSpan(Context context, String pinyin, float wordTextSize, @NonNull Typeface typeface) {

        this.pinyin = pinyin;

        float bopomofoTextSize = wordTextSize * BOPOMOFO_FONT_RATIO;

        pinyinPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        pinyinPaint.setTypeface(typeface);
        pinyinPaint.setTextSize(bopomofoTextSize);

        tonePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tonePaint.setTypeface(typeface);
        tonePaint.setTextSize(bopomofoTextSize * TONE_FONT_RATIO);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       @IntRange(from = 0) int start, @IntRange(from = 0) int end,
                       @Nullable Paint.FontMetricsInt fm) {

        final float pinyinSize = pinyinPaint.getTextSize();
        final float toneSize = tonePaint.getTextSize();

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();

        if (fm != null) {
            fm.ascent = fontMetrics.ascent;
            fm.descent = fontMetrics.descent;
            fm.top = fontMetrics.top;
            fm.bottom = fontMetrics.bottom;
            fm.leading = fontMetrics.leading;
        }

        wordWidth = paint.getTextSize();

        return (int)wordWidth + (int)pinyinSize + (int)toneSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     @IntRange(from = 0) int start, @IntRange(from = 0) int end,
                     float x, int top, int y, int bottom,
                     @NonNull Paint paint) {

        canvas.drawText(text, start, end, x, y, paint);
        drawPinyin(canvas, x, y, top);
    }

    private void drawPinyin(@NonNull Canvas canvas, float x, int y, int top) {

        boolean hasNeutral = false;
        boolean hasTone = false;

        int pinyinLength = pinyin.length();

        if (pinyin.charAt(0) == neutralTone) {
            hasNeutral = true;
            pinyinLength -= 1;
        }

        final char last = pinyin.charAt(pinyin.length() - 1);

        if (last == secondTone || last == thirdTone || last == fourthTone) {
            hasTone = true;
            pinyinLength -= 1;
        }

        final float pinyinTextSize = pinyinPaint.getTextSize();
        Paint.FontMetrics pinyinfontMetrics = pinyinPaint.getFontMetrics();

        final float startX = x + wordWidth;

        final float oneUnit = wordWidth * (1f/30f);
        final int topOffset = top;

        if (pinyinLength == 1) {

            float neutralOffset = topOffset + wordWidth * (1f/3f);
            float firstBaseline = neutralOffset + oneUnit - pinyinfontMetrics.ascent;

            if (hasNeutral) {
                // draw neutral tone
                float centerX = startX + pinyinTextSize * 0.5f;
                float centerY = topOffset + pinyinTextSize;
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint);

                pinyinPaint.setColor(Color.BLACK);
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint);

            } else {
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint);
            }

            if (hasTone) {
                final float toneStartX = startX + pinyinTextSize;
                final float toneBottom = topOffset + wordWidth * (14f/30f);
                Paint.FontMetrics toneFontMetrics = tonePaint.getFontMetrics();
                final float toneBaseline = toneBottom - toneFontMetrics.descent;

                canvas.drawText(pinyin, pinyin.length()-1, pinyin.length(), toneStartX,
                        toneBaseline, tonePaint);
            }
        } else if (pinyinLength == 2) {

            float neutralBottomOffset = topOffset + wordWidth * (5f/30f);
            float firstBaseline = neutralBottomOffset + oneUnit - pinyinfontMetrics.ascent;

            if (hasNeutral) {
                // draw neutral tone
                float centerX = startX + pinyinTextSize/2;
                float centerY = topOffset + neutralBottomOffset - oneUnit;
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint);

                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint);

                float secondBaseline = firstBaseline + oneUnit * 2 + pinyinTextSize;
                canvas.drawText(pinyin, 2, 3, startX, secondBaseline, pinyinPaint);
            } else {
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint);
                float secondBaseline = firstBaseline + oneUnit * 2 + pinyinTextSize;
                canvas.drawText(pinyin, 1, 2, startX, secondBaseline, pinyinPaint);
            }

            if (hasTone) {
                final float toneStartX = startX + pinyinTextSize;
                final float toneBottom = topOffset + wordWidth * (20f/30f);
                Paint.FontMetrics toneFontMetrics = tonePaint.getFontMetrics();
                final float toneBaseline = toneBottom - toneFontMetrics.descent;

                canvas.drawText(pinyin, pinyin.length()-1, pinyin.length(), toneStartX,
                        toneBaseline, tonePaint);
            }
        } else if (pinyinLength == 3) {
            if (hasNeutral) {
                // draw neutral tone
                float centerX = startX + pinyinTextSize * 0.5f;
                float centerY = topOffset + oneUnit;
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint);

                float firstBaseline = topOffset + wordWidth * (12f/30f) - pinyinfontMetrics.descent;
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint);
                canvas.drawText(pinyin, 2, 3, startX, firstBaseline + pinyinTextSize, pinyinPaint);
                canvas.drawText(pinyin, 3, 4, startX, firstBaseline + pinyinTextSize * 2, pinyinPaint);

            } else {
                float firstBaseline = topOffset + wordWidth * (11f/30f) - pinyinfontMetrics.descent;
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint);
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline + pinyinTextSize, pinyinPaint);
                canvas.drawText(pinyin, 2, 3, startX, firstBaseline + pinyinTextSize * 2, pinyinPaint);

                if (hasTone) {
                    final float toneStartX = startX + pinyinTextSize;
                    final float toneBottom = topOffset + wordWidth * (23f/30f);
                    Paint.FontMetrics toneFontMetrics = tonePaint.getFontMetrics();
                    final float toneBaseline = toneBottom - toneFontMetrics.descent;

                    canvas.drawText(pinyin, pinyin.length()-1, pinyin.length(), toneStartX,
                            toneBaseline, tonePaint);
                }
            }
        }
    }
}
