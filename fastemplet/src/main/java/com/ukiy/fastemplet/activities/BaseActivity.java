package com.ukiy.fastemplet.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by UKIY on 2016/4/6.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }
}
