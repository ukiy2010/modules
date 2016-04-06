package com.ukiy.fastemplet.myapp;

import android.app.Application;

import org.litepal.LitePalApplication;

/**
 * Created by UKIY on 2016/4/6.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
    }
}
