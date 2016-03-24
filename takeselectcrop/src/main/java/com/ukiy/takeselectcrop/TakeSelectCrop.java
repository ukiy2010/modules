package com.ukiy.takeselectcrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by UKIY on 2016/3/23.
 */
public class TakeSelectCrop {
    public static final int TAKE_PHOTO = 32210;
    public static final int SELECT_PHOTO = 23814;
    public static final int CROP_PHOTO = 19258;
    private final String TAG = "TakeSelectCrop";
    private final String ORIGIN_FILENAME = "origin.jpg";
    private final String CROP_FILENAME = "crop.jpg";
    private final String RESIZE_FILENAME = "resize.jpg";

    private Uri originUri;
    private Uri cropUri;
    private Uri resizeUri;

    private Activity activity;
    private int aspectX = 0;
    private int aspectY = 0;
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private Callback callback = new Callback() {
        @Override
        public void onTaked(Uri photoFileUri) {

        }

        @Override
        public void onSelected(Uri photoFileUri) {

        }

        @Override
        public void onCropped(Uri photoFileUri) {

        }

        @Override
        public void onResized(Uri photoFileUri) {

        }

        @Override
        public void onCanceled(int requestCode) {

        }
    };

    public TakeSelectCrop(Activity activity) {
        this.activity = activity;
        originUri = getPhotoFileUri(ORIGIN_FILENAME);
        cropUri = getPhotoFileUri(CROP_FILENAME);
        resizeUri = getPhotoFileUri(RESIZE_FILENAME);
    }

    public TakeSelectCrop setAspect(int x, int y) {
        this.aspectX = x;
        this.aspectY = y;
        return this;
    }

    public TakeSelectCrop setMaxSize(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        return this;
    }

    public TakeSelectCrop setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, originUri);
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, SELECT_PHOTO);
    }

    public void cropPhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", maxWidth);
//        intent.putExtra("outputY", maxHeight);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, CROP_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        switch (requestCode) {
            case TAKE_PHOTO:
                handleTake(requestCode, resultCode, result);
                break;
            case SELECT_PHOTO:
                handleSelect(requestCode, resultCode, result);
                break;
            case CROP_PHOTO:
                handleCrop(requestCode, resultCode, result);
                break;
            default:
                break;
        }
    }

    private void handleTake(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onCanceled(requestCode);
            return;
        } else if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "take photo not success");
            return;
        }
        callback.onTaked(originUri);
    }

    private void handleSelect(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onCanceled(requestCode);
            return;
        } else if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "select photo not success");
            return;
        }
        //将选取的图片文件复制到安全目录，这样可以保持和处理拍照图片逻辑的一致性
        try {
            InputStream is = activity.getContentResolver().openInputStream(result.getData());
            OutputStream os = activity.getContentResolver().openOutputStream(originUri);
            byte[] buf = new byte[8192];
            int readLen;
            while ((readLen = is.read(buf)) != -1) {
                os.write(buf, 0, readLen);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.onSelected(originUri);
    }

    private void handleCrop(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onCanceled(requestCode);
            return;
        } else if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "crop photo not success");
            return;
        }
        callback.onCropped(cropUri);
        callback.onResized(bitmapToFile(sampleBitmap(cropUri), resizeUri));
    }

    // 获取外存安全目录中指定文件名的uri
    private Uri getPhotoFileUri(String fileName) {
        String state = Environment.getExternalStorageState();
        // 外部存储设备挂载成功且有读写权限
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 获取安全存储路径，getExternalFilesDir获取的是应用包名对应的外存目录，读写这个目录不需要声明外存读写权限
            File mediaStorageDir = new File(
                    activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
            // 临时目录不存在就创建
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
            }
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    //对输入流取样，压缩图片使之不超过最大长宽值
    private Bitmap sampleBitmap(Uri in) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        String string = in.getPath();
        BitmapFactory.decodeFile(string, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        int sw = w / maxWidth;
        int sh = h / maxHeight;
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = Math.max(Math.max(sw, sh), 1);
        return BitmapFactory.decodeFile(in.getPath(), opt);
    }

    private Uri bitmapToFile(Bitmap bm, Uri out) {
        if (bm == null) return null;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(activity.getContentResolver().openOutputStream(out));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out;
    }

    public interface Callback {
        void onTaked(Uri photoFileUri);//当拍照成功时回调

        void onSelected(Uri photoFileUri);//当选择成功时回调

        void onCropped(Uri photoFileUri);//当剪裁成功时回调

        void onResized(Uri photoFileUri);//当缩放成功时回调

        void onCanceled(int requestCode);//当取消时回调
    }
}
