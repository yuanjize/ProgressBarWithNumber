package com.yuan.numberprogressbardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 仿制代码家的NumberProgressBar
 * Created by yjz on 2016/8/25.
 */
public class MyProgressBar extends View {
    //已到达区域的画笔
    private Paint mReachesAreaPaint = new Paint();
    //未到达区域的画笔
    private Paint mUnReachesAreaPaint = new Paint();
    //文字画笔
    private Paint mTextPaint = new Paint();
    //已到达的区域
    private RectF reachedRect = new RectF();
    //未到达的区域
    private RectF unReachedRect = new RectF();
    //进度条的高度
    private float barHeight = 0;
    //字体大小
    private float textSize;
    //字体颜色
    private int textColor;
    //到达区域的颜色
    private int reachedColor;
    //未到达区域的颜色
    private int unReachedColor;
    //darwText的参数书
    private float textStart = 0;
    private float textEnd;

    //进度条圆角弧度
    private float arc = 10;


    public void setMaxValue(float maxValue) {
        if (maxValue >= 0)
            this.maxValue = maxValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress <= maxValue && progress >= 0)
            this.progress = progress;
        invalidate();
    }

    private float maxValue = 100f;
    private float progress = 0;

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar);
        reachedColor = typedArray.getColor(R.styleable.MyProgressBar_reach_Color, Color.BLUE);
        unReachedColor = typedArray.getColor(R.styleable.MyProgressBar_unReach_Color, Color.RED);
        barHeight = typedArray.getDimension(R.styleable.MyProgressBar_bar_Height, dp2px(10));
        textSize = typedArray.getDimension(R.styleable.MyProgressBar_textSize, sp2px(10));
        textColor = typedArray.getColor(R.styleable.MyProgressBar_textColor, Color.GRAY);
        arc = typedArray.getFloat(R.styleable.MyProgressBar_arc, arc);
        initPaints();
        typedArray.recycle();

    }

    private void initPaints() {
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);
        mReachesAreaPaint.setAntiAlias(true);
        mUnReachesAreaPaint.setAntiAlias(true);
        mReachesAreaPaint.setColor(reachedColor);
        mUnReachesAreaPaint.setColor(unReachedColor);
        mReachesAreaPaint.setStyle(Paint.Style.FILL);
        mUnReachesAreaPaint.setStyle(Paint.Style.FILL);
    }

    public MyProgressBar(Context context) {
        super(context, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureMyself(widthMeasureSpec, true), measureMyself(heightMeasureSpec, false));
    }

    private int getWrapWidth() {
        return (int) textSize;
    }

    private int getWrapHeight() {
        return (int) Math.max(barHeight, textSize);
    }


    private int measureMyself(int measureSpec, boolean isWidth) {
        int value = 0;
        int padding = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (isWidth) {
            padding = getPaddingLeft() + getPaddingRight();
        } else {
            padding = getPaddingTop() + getPaddingBottom();
        }
        if (mode == MeasureSpec.AT_MOST) {
            value = isWidth ? getWrapWidth() : getWrapHeight();
            value += padding;
        } else if (MeasureSpec.EXACTLY == mode) {
            value = size;
        }
        return value;
    }

    private boolean hasNoSpace = false;

    @Override
    protected void onDraw(Canvas canvas) {
        String text = (int) ((getProgress()) / maxValue * 100) + "%";
        float measureText = mTextPaint.measureText(text);
        reachedRect.left = getPaddingLeft();
        reachedRect.top = (getHeight() - barHeight) / 2;
        reachedRect.bottom = barHeight + reachedRect.top;
        reachedRect.right = getPaddingLeft() + ((getWidth() - getPaddingLeft() - getPaddingRight()) / getMaxValue() * getProgress());
        textStart = reachedRect.right;

        if ((textStart + measureText) > (getWidth() - getPaddingRight())) {
            textStart = getWidth() - getPaddingRight() - measureText;
            hasNoSpace = true;
        }
        textEnd = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));
        if (!hasNoSpace) {
            unReachedRect.left = textStart + measureText;
            unReachedRect.top = reachedRect.top;
            unReachedRect.bottom = reachedRect.bottom;
            unReachedRect.right = getWidth() - getPaddingRight();
        }
        canvas.drawRoundRect(reachedRect, arc, arc, mReachesAreaPaint);
        canvas.drawRoundRect(unReachedRect, arc, arc, mUnReachesAreaPaint);
        canvas.drawText(text, textStart, textEnd, mTextPaint);
    }

    public void setReachedColor(int reachedColor) {
        this.reachedColor = reachedColor;
        mReachesAreaPaint.setColor(reachedColor);
        invalidate();
    }

    public void setUnReachedColor(int unReachedColor) {
        this.unReachedColor = unReachedColor;
        mUnReachesAreaPaint.setColor(unReachedColor);
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }
}