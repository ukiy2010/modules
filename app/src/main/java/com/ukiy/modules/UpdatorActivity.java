package com.ukiy.modules;

import android.os.Bundle;
import android.view.View;

public class UpdatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updator);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                break;
            default:
                break;
        }
    }
}
