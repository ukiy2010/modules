package com.ukiy.modules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_TakeSelectCrop:
                startActivity(new Intent(MainActivity.this, TakeSelectCropActivity.class));
                break;
            case R.id.btn_Updator:
                break;
            default:
                break;
        }
    }
}
