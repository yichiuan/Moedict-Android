package com.yichiuan.moedict.common.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.*
import android.util.AttributeSet
import android.view.View
import com.yichiuan.moedict.R
import com.yichiuan.moedict.common.FontCache


class StaticTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val COMMA = '，'
        private const val WORD_FONT = "font/NotoSansTC-Regular.otf"
        private const val BOPOMOFO_FONT = "font/eduSong_Unicode.ttf"
    }

    private lateinit var text: CharSequence
    private lateinit var pinyin: String
    private var layout: StaticLayout? = null
    private val paint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private val spacingMult: Float

    private fun getDesiredHeight(): Int = layout!!.height

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StaticTextView)
        val textSize = typedArray.getDimensionPixelSize(R.styleable.StaticTextView_text_size, 20)
        spacingMult = typedArray.getFloat(R.styleable.StaticTextView_line_spacing_multiplier, 1.0f)
        typedArray.recycle()

        paint.apply {
            typeface = FontCache.getTypeface(getContext(), WORD_FONT)
            this.textSize = textSize.toFloat()
            density = resources.displayMetrics.density
        }

        if (isInEditMode) {
            setText(SpannableStringBuilder("甜蜜"))
            setPinyin("ㄊㄧㄢˊ　ㄇㄧˋ")
        } else {
            text = ""
        }
    }

    fun setText(text: CharSequence) {
        this.text = text
        if (layout != null) {
            invalidate()
            requestLayout()
        }
        layout = null
    }

    fun setPinyin(pinyin: String) {
        this.pinyin = pinyin
        val pinyins = pinyin.split("[ 　]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val spannableString = text as SpannableStringBuilder?

        val textSize = paint.textSize

        val pinyinTypeface: Typeface? = if (isInEditMode) {
            Typeface.SANS_SERIF
        } else {
            FontCache.getTypeface(context, BOPOMOFO_FONT)
        }

        var pinyinIndex = 0
        for (i in 0 until text.length) {
            if (text[i] != COMMA) {
                val pinyinSpan = PinyinSpan(context, pinyins[pinyinIndex],
                        textSize, pinyinTypeface!!)
                spannableString!!.setSpan(pinyinSpan, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                ++pinyinIndex
            }
        }

        if (layout != null) {
            invalidate()
            requestLayout()
        }
        layout = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        var height: Int

        if (widthMode == View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize

            // TODO check
            if (layout == null) {
                layout = StaticLayout(text, paint, width,
                        Layout.Alignment.ALIGN_NORMAL, spacingMult, 0f, false)
            }
        } else {
            width = widthSize
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize
        } else {
            val desired: Int
            if (layout != null) {
                desired = getDesiredHeight()
                height = desired
            } else {
                desired = heightSize
                height = desired
            }

            if (heightMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(desired, heightSize)
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        layout?.draw(canvas)
    }
}
