package com.ukiy.test.progresstextview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 使用ProgressTextView的一个集合View
 * Created by ukiy on 16/5/16.
 */
public class ProgressBundleView extends FrameLayout {

    private TextView tv_top;
    private ProgressTextView ptv;
    private ImageView iv;

    public ProgressBundleView(Context context) {
        this(context, null);
    }

    public ProgressBundleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBundleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_progress_bundle_view, this);
        tv_top = (TextView) view.findViewById(R.id.tv_top);
        ptv = (ProgressTextView) view.findViewById(R.id.ptv);
        iv = (ImageView) view.findViewById(R.id.iv);
    }

    public TextView getTopTextView() {
        return tv_top;
    }

    public ImageView getImageView() {
        return iv;
    }

    public ProgressTextView getProgressTextView() {
        return ptv;
    }
}
