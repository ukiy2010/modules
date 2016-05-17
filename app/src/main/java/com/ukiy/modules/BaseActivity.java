package com.ukiy.modules;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by UKIY on 2016/3/23.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public <T extends View> T $(int id) {
        View ret = findViewById(id);
        return (T) ret;
    }
}
