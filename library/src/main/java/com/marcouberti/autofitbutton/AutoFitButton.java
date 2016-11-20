package com.marcouberti.autofitbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
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
    private float textSize, minTextSize, maxTextSize;

    public AutoFitButton(Context context) {
        super(context);
        init();
    }

    public AutoFitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AutoFitButton,
                0, 0);

        try {
            maxTextSize = a.getDimensionPixelSize(R.styleable.AutoFitButton_maxTextSize, -1);
            minTextSize = a.getDimensionPixelSize(R.styleable.AutoFitButton_minTextSize, -1);
        } finally {
            a.recycle();
        }
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
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.text = text.toString();
        setBestTextSize(getInnerWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int innerWidth = getInnerWidth();
        setBestTextSize(innerWidth);
    }

    private int getInnerWidth() {
        //Compute the available inner space for text
        //consider: - padding
        //          - drawables
        Drawable[] drawable = getCompoundDrawables();//left, top, right, and bottom
        Drawable leftDrawable = drawable[0];
        Drawable rightDrawable = drawable[2];

        int ldw = 0, rdw = 0;
        if(leftDrawable != null) {
            ldw = leftDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        if(rightDrawable != null) {
            rdw = rightDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        int innerWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - ldw - rdw;
        return innerWidth;
    }

    private void setBestTextSize(int innerWidth) {
        if(innerWidth <= 0) return;

        this.textSize = this.getTextSize();

        TextPaint paint = this.getPaint();
        boolean found = false;
        while (!found && this.textSize > 0 && this.textSize >= this.minTextSize) {
            paint.setTextSize(this.textSize);
            CharSequence charSequence;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(this.text, Html.FROM_HTML_MODE_COMPACT);
            }else {
                charSequence = Html.fromHtml(this.text);
            }
            StaticLayout sl = new StaticLayout(charSequence, paint, innerWidth, Layout.Alignment.ALIGN_CENTER, 0, 0, true);
            if(sl.getLineCount() <= 1) {
                found = true;
            }else {
                this.textSize -= 1f;
            }
        }

        this.setTextSize(((this.textSize -1f) / getResources().getDisplayMetrics().density));
    }
}
