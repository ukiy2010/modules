package com.ukiy.modules;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by UKIY on 2016/3/23.
 */
public class BaseActivity extends AppCompatActivity {
    public <T extends View> T $(int id) {
        View ret = findViewById(id);
        return (T) ret;
    }
}
