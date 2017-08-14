package com.yichiuan.moedict.common.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.common.FontCache;
import com.yichiuan.moedict.common.ui.PinyinSpan;


public class StaticTextView extends View {

    private static final char COMMA = '，';

    private static final String WORD_FONT = "font/NotoSansTC-Regular.otf";
    private static final String BOPOMOFO_FONT = "font/eduSong_Unicode.ttf";

    private CharSequence text;
    private String pinyin;
    private StaticLayout layout;
    private TextPaint paint;

    private float spacingMult;

    public StaticTextView(Context context) {
        this(context, null);
    }

    public StaticTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaticTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources res = getResources();
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.density = res.getDisplayMetrics().density;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StaticTextView);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.StaticTextView_text_size, 20);
        spacingMult = typedArray.getFloat(R.styleable.StaticTextView_line_spacing_multiplier, 1.0f);
        typedArray.recycle();

        Typeface typeface = FontCache.getTypeface(getContext(), WORD_FONT);

        paint.setTypeface(typeface);
        paint.setTextSize(textSize);

        if (isInEditMode()) {
            setText(new SpannableStringBuilder("甜蜜"));
            setPinyin("ㄊㄧㄢˊ　ㄇㄧˋ");
        } else {
            text = "";
        }
    }

    public void setText(CharSequence text) {
        this.text = text;
        if (layout != null) {
            invalidate();
            requestLayout();
        }
        layout = null;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String[] pinyins = pinyin.split("[ 　]");

        SpannableStringBuilder spannableString = (SpannableStringBuilder) text;

        final float textSize = paint.getTextSize();

        Typeface pinyinTypeface;

        if (isInEditMode()) {
            pinyinTypeface = Typeface.SANS_SERIF;
        } else {
            pinyinTypeface = FontCache.getTypeface(getContext(), BOPOMOFO_FONT);
        }

        int pinyinIndex = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) != COMMA) {
                PinyinSpan pinyinSpan = new PinyinSpan(getContext(), pinyins[pinyinIndex],
                        textSize, pinyinTypeface);
                spannableString.setSpan(pinyinSpan, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                ++pinyinIndex;
            }
        }

        if (layout != null) {
            invalidate();
            requestLayout();
        }
        layout = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;

            // TODO check
            if (layout == null) {
                layout = new StaticLayout(text, paint, width,
                        Layout.Alignment.ALIGN_NORMAL, spacingMult, 0, false);
            }
        } else {
            width = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize;
        } else {
            int desired;
            if (layout != null) {
                desired = getDesiredHeight();
                height = desired;
            } else {
                desired = heightSize;
                height = desired;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (layout != null) {
            layout.draw(canvas);
        }
    }

    private int getDesiredHeight() {
        int desired = layout.getHeight();
        return desired;
    }
}
