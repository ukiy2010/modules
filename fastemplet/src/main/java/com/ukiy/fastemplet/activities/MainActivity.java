package com.ukiy.fastemplet.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.ukiy.fastemplet.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = $(R.id.tv);
    }
}
