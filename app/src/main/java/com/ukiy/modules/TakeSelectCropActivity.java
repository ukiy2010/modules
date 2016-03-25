package com.ukiy.modules;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ukiy.takeselectcrop.TakeSelectCrop;

public class TakeSelectCropActivity extends BaseActivity {
    ImageView iv;
    TakeSelectCrop takeSelectCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_select_crop);
        iv = $(R.id.iv);
        takeSelectCrop = new TakeSelectCrop(this).setAspect(16, 9).setMaxSize(1920, 1080)
                .setCallback(new TakeSelectCrop.Callback() {
                    @Override
                    public void onTaked(Uri photoFileUri) {
                        takeSelectCrop.cropPhoto();
                    }

                    @Override
                    public void onSelected(Uri photoFileUri) {
                        takeSelectCrop.cropPhoto();
                    }

                    @Override
                    public void onCropped(Uri photoFileUri) {
                    }

                    @Override
                    public void onResized(Uri photoFileUri) {
                        iv.setImageURI(null);
                        iv.setImageURI(photoFileUri);
                    }

                    @Override
                    public void onCanceled(int requestCode) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take:
                takeSelectCrop.takePhoto();
                break;
            case R.id.btn_select:
                takeSelectCrop.selectPhoto();
                break;
            case R.id.iv:
                takeSelectCrop.cropPhoto();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takeSelectCrop.onActivityResult(requestCode, resultCode, data);
    }
}
