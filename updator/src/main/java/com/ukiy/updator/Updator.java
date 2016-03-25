package com.ukiy.updator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by UKIY on 2016/3/25.
 */
public class Updator {
    private static final String TAG = "Updator";
    private static String url;
    private static String cur_version;
    private static boolean isInit = false;

    public static void init(String cur_version, String url) {
        Updator.url = url;
        Updator.cur_version = cur_version;
        isInit = true;
    }

    public static String getUrl(){
        return Updator.url;
    }
    public static String getCur_version() {
        return Updator.cur_version;
    }

    public static void check(Context context, long period, CheckCallback checkCallback) {
        if (!isInit) {
            throw new RuntimeException("Updator is not yet init!");
        }
        UpdatorService.check(context, period, checkCallback);
    }

    public static void download(Context context, String url, DownloadCallback downloadCallback) {
        if (!isInit) {
            throw new RuntimeException("Updator is not yet init!");
        }
        UpdatorService.download(context, url, downloadCallback);
    }

    public static void install(Context context) {
        final File f = getUpdatePath(context);
        if (f == null) return;
        if (!f.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static File getUpdatePath(Context context) {
        String state = Environment.getExternalStorageState();
        // 外部存储设备挂载成功且有读写权限
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 获取安全存储路径，getExternalFilesDir获取的是应用包名对应的外存目录，读写这个目录不需要声明外存读写权限
            File mediaStorageDir = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
            // 临时目录不存在就创建
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
            }
            return new File(mediaStorageDir.getPath() + File.separator + "update.apk");
        }
        Log.d(TAG, "media is not mounted");
        return null;
    }

    public static void start(Context context) {
        if (!isInit) {
            throw new RuntimeException("Updator is not yet init!");
        }
        check(context, 0, new CheckCallback() {

            @Override
            public void onOptUpdate(Context context, UpdateInfo updateInfo, String cur_version) {

            }

            @Override
            public void onMustUpdate(Context context, UpdateInfo updateInfo, String cur_version) {

            }

            @Override
            public void onAlreadyNewest(Context context, UpdateInfo updateInfo, String cur_version) {

            }

            @Override
            public void onFail(Context context) {

            }
        });
    }
}
