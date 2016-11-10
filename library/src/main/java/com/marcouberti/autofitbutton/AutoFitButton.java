package com.marcouberti.autofitbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * AutoFitButton
 * Created by Marco on 04/11/2016.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public class AutoFitButton extends Button {

    private String text = "";
    private float textSize;
    private Paint textPaint;
    private int ldw = 0, rdw = 0;

    public AutoFitButton(Context context) {
        super(context);
        init();
    }

    public AutoFitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoFitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        CharSequence cs = this.getText();
        if (cs != null) {
            this.text = this.getText().toString();
            if(this.getTransformationMethod() != null) {
                this.text = this.getTransformationMethod().getTransformation(getText(), this).toString();
            }
        }
        this.textSize = this.getTextSize();
        textPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Drawable[] drawable = getCompoundDrawables();//left, top, right, and bottom
        Drawable leftDrawable = drawable[0];
        Drawable rightDrawable = drawable[2];

        if(leftDrawable != null) {
            ldw = leftDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        if(rightDrawable != null) {
            rdw = rightDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        int innerWidth = w - getPaddingLeft() - getPaddingRight() - ldw - rdw;
        setBestTextSize(innerWidth);
    }

    private void setBestTextSize(int innerWidth) {
        this.textSize = this.getTextSize();

        Paint paint = this.getPaint();
        float textWidth = paint.measureText(this.text);
        while (textWidth >= innerWidth && this.textSize > 0) {
            paint.setTextSize(this.textSize);
            textWidth = paint.measureText(this.text);
            if(textWidth <= innerWidth) {
                break;
            }else {
                this.textSize -= 1f;
            }
        }

        this.setTextSize(((this.textSize -1f) / getResources().getDisplayMetrics().density));
        this.forceLayout();
    }

    private final Rect textBounds = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int textColor;
        //DISABLED
        if(!isEnabled()) {
            int[] states = new int[] {-android.R.attr.state_enabled};
            textColor = getTextColors().getColorForState(states, Color.BLACK);
        }
        //PRESSED
        else if(isPressed()) {
            int[] states = new int[] {android.R.attr.state_pressed};
            textColor = getTextColors().getColorForState(states, Color.BLACK);
        }
        //FOCUSED
        else if(isFocused()) {
            int[] states = new int[] {android.R.attr.state_focused};
            textColor = getTextColors().getColorForState(states, Color.BLACK);
        }
        //NORMAL
        else {
            int[] states = new int[] {android.R.attr.state_enabled};
            textColor = getTextColors().getColorForState(states, Color.BLACK);
        }

        textPaint.getTextBounds(this.text, 0, text.length(), textBounds);

        textPaint.setTypeface(this.getTypeface());
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(this.textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        int innerWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - ldw - rdw;
        canvas.drawText(this.text, getPaddingLeft() + ldw + innerWidth/2f, getMeasuredHeight() / 2.0f - textBounds.exactCenterY(), textPaint);
    }
}
