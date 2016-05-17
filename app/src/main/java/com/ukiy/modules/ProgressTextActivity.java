package com.ukiy.modules;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ProgressTextActivity extends AppCompatActivity {

    private TextView tvv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_text);
        tvv=(TextView)findViewById(R.id.tvv);
        Drawable drawable = tvv.getBackground();
        ClipDrawable clipDrawable=new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        clipDrawable.setLevel(3000);
        tvv.setBackgroundDrawable(clipDrawable);
    }
}
