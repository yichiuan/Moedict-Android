package com.yichiuan.moedict.common.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.annotation.IntRange
import android.text.TextPaint
import android.text.style.ReplacementSpan


class PinyinSpan(context: Context, private val pinyin: String, wordTextSize: Float,
                 typeface: Typeface) : ReplacementSpan() {

    companion object {

        private const val neutralTone = '˙'
        private const val secondTone = 'ˊ'
        private const val thirdTone = 'ˇ'
        private const val fourthTone = 'ˋ'

        private const val BOPOMOFO_FONT_RATIO = 9f / 30f
        private const val TONE_FONT_RATIO = 5f / 9f
    }

    private val pinyinPaint: TextPaint

    private val tonePaint: TextPaint

    private var wordWidth: Float = 0f

    init {

        val bopomofoTextSize = wordTextSize * BOPOMOFO_FONT_RATIO

        pinyinPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        pinyinPaint.typeface = typeface
        pinyinPaint.textSize = bopomofoTextSize

        tonePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        tonePaint.typeface = typeface
        tonePaint.textSize = bopomofoTextSize * TONE_FONT_RATIO
    }

    override fun getSize(paint: Paint, text: CharSequence,
                         @IntRange(from = 0) start: Int, @IntRange(from = 0) end: Int,
                         fm: Paint.FontMetricsInt?): Int {

        val pinyinSize = pinyinPaint.textSize
        val toneSize = tonePaint.textSize

        val fontMetrics = paint.fontMetricsInt

        fm?.apply {
            ascent = fontMetrics.ascent
            descent = fontMetrics.descent
            top = fontMetrics.top
            bottom = fontMetrics.bottom
            leading = fontMetrics.leading
        }

        wordWidth = paint.textSize

        return wordWidth.toInt() + pinyinSize.toInt() + toneSize.toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence,
                      @IntRange(from = 0) start: Int, @IntRange(from = 0) end: Int,
                      x: Float, top: Int, y: Int, bottom: Int,
                      paint: Paint) {

        canvas.drawText(text, start, end, x, y.toFloat(), paint)
        drawPinyin(canvas, x, y, top)
    }

    private fun drawPinyin(canvas: Canvas, x: Float, y: Int, top: Int) {

        var hasNeutral = false
        var hasTone = false

        var pinyinLength = pinyin.length

        if (pinyin[0] == neutralTone) {
            hasNeutral = true
            pinyinLength -= 1
        }

        val last = pinyin[pinyin.length - 1]

        if (last == secondTone || last == thirdTone || last == fourthTone) {
            hasTone = true
            pinyinLength -= 1
        }

        val pinyinTextSize = pinyinPaint.textSize
        val pinyinfontMetrics = pinyinPaint.fontMetrics

        val startX = x + wordWidth

        val oneUnit = wordWidth * (1f / 30f)

        if (pinyinLength == 1) {

            val neutralOffset = top + wordWidth * (1f / 3f)
            val firstBaseline = neutralOffset + oneUnit - pinyinfontMetrics.ascent

            if (hasNeutral) {
                // draw neutral tone
                val centerX = startX + pinyinTextSize * 0.5f
                val centerY = top + pinyinTextSize
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint)

                pinyinPaint.color = Color.BLACK
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint)

            } else {
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint)
            }

            if (hasTone) {
                val toneStartX = startX + pinyinTextSize
                val toneBottom = top + wordWidth * (14f / 30f)
                val toneFontMetrics = tonePaint.fontMetrics
                val toneBaseline = toneBottom - toneFontMetrics.descent

                canvas.drawText(pinyin, pinyin.length - 1, pinyin.length, toneStartX,
                        toneBaseline, tonePaint)
            }
        } else if (pinyinLength == 2) {

            val neutralBottomOffset = top + wordWidth * (5f / 30f)
            val firstBaseline = neutralBottomOffset + oneUnit - pinyinfontMetrics.ascent

            if (hasNeutral) {
                // draw neutral tone
                val centerX = startX + pinyinTextSize / 2
                val centerY = top + neutralBottomOffset - oneUnit
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint)

                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint)

                val secondBaseline = firstBaseline + oneUnit * 2 + pinyinTextSize
                canvas.drawText(pinyin, 2, 3, startX, secondBaseline, pinyinPaint)
            } else {
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint)
                val secondBaseline = firstBaseline + oneUnit * 2 + pinyinTextSize
                canvas.drawText(pinyin, 1, 2, startX, secondBaseline, pinyinPaint)
            }

            if (hasTone) {
                val toneStartX = startX + pinyinTextSize
                val toneBottom = top + wordWidth * (20f / 30f)
                val toneFontMetrics = tonePaint.fontMetrics
                val toneBaseline = toneBottom - toneFontMetrics.descent

                canvas.drawText(pinyin, pinyin.length - 1, pinyin.length, toneStartX,
                        toneBaseline, tonePaint)
            }
        } else if (pinyinLength == 3) {
            if (hasNeutral) {
                // draw neutral tone
                val centerX = startX + pinyinTextSize * 0.5f
                val centerY = top + oneUnit
                canvas.drawCircle(centerX, centerY, oneUnit, pinyinPaint)

                val firstBaseline = top + wordWidth * (12f / 30f) - pinyinfontMetrics.descent
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline, pinyinPaint)
                canvas.drawText(pinyin, 2, 3, startX, firstBaseline + pinyinTextSize, pinyinPaint)
                canvas.drawText(pinyin, 3, 4, startX, firstBaseline + pinyinTextSize * 2, pinyinPaint)

            } else {
                val firstBaseline = top + wordWidth * (11f / 30f) - pinyinfontMetrics.descent
                canvas.drawText(pinyin, 0, 1, startX, firstBaseline, pinyinPaint)
                canvas.drawText(pinyin, 1, 2, startX, firstBaseline + pinyinTextSize, pinyinPaint)
                canvas.drawText(pinyin, 2, 3, startX, firstBaseline + pinyinTextSize * 2, pinyinPaint)

                if (hasTone) {
                    val toneStartX = startX + pinyinTextSize
                    val toneBottom = top + wordWidth * (23f / 30f)
                    val toneFontMetrics = tonePaint.fontMetrics
                    val toneBaseline = toneBottom - toneFontMetrics.descent

                    canvas.drawText(pinyin, pinyin.length - 1, pinyin.length, toneStartX,
                            toneBaseline, tonePaint)
                }
            }
        }
    }
}
