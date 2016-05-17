package com.ukiy.test.progresstextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ukiy on 16/5/16.
 */
public class ProgressTextView extends TextView {
    private Drawable progressBackground;//背景
    private Drawable progressForeground;//前景

    private int mProgress = 0;//百分比，从0到100

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView);
        progressBackground = ta.getDrawable(R.styleable.ProgressTextView_progressBackground);
        progressForeground = ta.getDrawable(R.styleable.ProgressTextView_progressForeground);
        mProgress = ta.getInteger(R.styleable.ProgressTextView_progress, 0);
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            update();
        }
    }

    /**
     * 更新ui
     */
    private void update() {
        //progressBackground和progressForeground必须同时被设置才会生效
        if (progressBackground != null && progressForeground != null) {
            //背景在下面，前景在上面
            Drawable[] drawables = {progressBackground, progressForeground};
            LayerDrawable layerDrawable = new LayerDrawable(drawables);
            //后面四个参数分别为左，上，右，下的padding值，中间的图形会挤压变形（和clip有区别，clip图片不变形）
            layerDrawable.setLayerInset(1, 0, 0, getWidth() - getWidth() * mProgress / 100, 0);
            setBackgroundDrawable(layerDrawable);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public Drawable getProgressBackground() {
        return progressBackground;
    }

    public void setProgressBackground(Drawable progressBackground) {
        this.progressBackground = progressBackground;
        update();
    }

    public Drawable getProgressForeground() {
        return progressForeground;
    }

    public void setProgressForeground(Drawable progressForeground) {
        this.progressForeground = progressForeground;
        update();
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        update();
    }
}
