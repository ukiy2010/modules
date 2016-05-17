package com.ukiy.modules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

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
                startActivity(new Intent(MainActivity.this, UpdatorActivity.class));
                break;
            case R.id.btn_ProgressTextView:
                startActivity(new Intent(MainActivity.this, ProgressTextActivity.class));
                break;
            default:
                break;
        }
    }
}
