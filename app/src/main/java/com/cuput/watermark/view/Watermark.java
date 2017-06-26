package com.cuput.watermark.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cuput.watermark.R;
import com.cuput.watermark.utils.DisplayUtil;

/**
 * Created by wei on 2017-06-22.
 */

public class Watermark extends View {
    private final Context mContext;
    private Paint mPaint;
    private int viewWidth;
    private int viewHeight;

    // custom styleable
    private String text; // 水印文字
    private int textColor; // 文字颜色
    private float textSize; // 文字大小
    private int textAlpha; // 透明度 0~255
    private float textStrokeWidth; // 画笔粗细
    private float textAngle; // 旋转度数

    public Watermark(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public Watermark(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        getAttrs(context, attributeSet);
        initPaint();
    }

    public Watermark(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        mContext = context;
        getAttrs(context, attributeSet);
        initPaint();
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attributeSet
     */
    private void getAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Watermark);
        this.text = typedArray.getString(R.styleable.Watermark_text);
        this.textColor = typedArray.getColor(R.styleable.Watermark_textColor, context.getResources().getColor(R.color.textColor));
        this.textSize = typedArray.getDimension(R.styleable.Watermark_textSize, 20F);
        this.textAlpha = typedArray.getInt(R.styleable.Watermark_textAlpha, 120);
        this.textStrokeWidth = typedArray.getFloat(R.styleable.Watermark_textStrokeWidth, 3.0F);
        this.textAngle = typedArray.getFloat(R.styleable.Watermark_textAngle, 25);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(this.textColor);
        mPaint.setAlpha(this.textAlpha);
        mPaint.setStrokeWidth(this.textStrokeWidth);
        mPaint.setTextSize(DisplayUtil.sp2px(mContext, this.textSize));

//        mPaint.setColor(Color.parseColor("#CCCCCC"));
//        mPaint.setAlpha(120);
//        mPaint.setStrokeWidth(3.0F);
//        mPaint.setTextSize(40F);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = super.getMeasuredWidth();
        viewHeight = super.getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = this.text = "工号123B4987";
        int textWidth = this.getStringWidth(text);
        int textHeight = (viewHeight / (viewHeight / 120));
        // L = nπr / 180 计算弧长
        int l = (int) ((this.textAngle * Math.PI * (viewWidth / 2)) / 180);

        int x = 0;
        int y = textHeight / 2;
        boolean flag = true;
        while (flag) {
            if (x > viewWidth + l) {
                x = x - viewWidth - textWidth - l;
                y += textHeight * 2;
            }
            this.drawText(canvas, text, x, y, mPaint, -this.textAngle);
            x += (textWidth + (textWidth - textWidth / 4));

            // 计算所得大于弧长即可，但并未铺满，所以加大倍数
            if (x > viewWidth && y - viewHeight > l * 2) {
                flag = false;
            }
        }
    }

    private void drawText(Canvas canvas, String text, int x, int y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, 0, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, 0, y);
        }
    }

    private int getStringWidth(String text) {
        return (int)mPaint.measureText(text);
    }
}
