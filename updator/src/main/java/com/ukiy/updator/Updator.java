package com.ukiy.updator;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * Created by UKIY on 2016/3/25.
 */
public class Updator {
    private static final String TAG = "Updator";
    private static volatile Updator singleton;
    private String url;
    private String curVersion;

    private Context mContext;
    private HandlerThread subThread;
    private Handler subTheadHandler;
    private Handler mainHandler;

    private boolean isDownloading = false;
    private boolean isChecking = false;
    private boolean running = true;

    private Updator(Context context, String curVersion, String url) {
        this.url = url;
        this.curVersion = curVersion;
        this.mContext = context;
        subThread = new HandlerThread(TAG);
        subThread.start();
        subTheadHandler = new Handler(subThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static void init(Context context, String curVersion, String url) {
        if (singleton != null) {
            throw new RuntimeException("You can init Updator only once!");
        }
        if (!(context instanceof Application)) {
            throw new RuntimeException("context must be application!");
        }
        if (singleton == null) {
            synchronized (Updator.class) {
                if (singleton == null)
                    singleton = new Updator(context, curVersion, url);
            }
        }
    }

    private static void checkInit() {
        if (singleton == null) {
            throw new RuntimeException("Please init Updator before you use it!");
        }
    }

    public static String getUrl() {
        return singleton.url;
    }

    public static String getCurVersion() {
        return singleton.curVersion;
    }

    public static void quit() {
        if (singleton == null) return;
        singleton.running = false;
        singleton.subTheadHandler.removeCallbacksAndMessages(null);
        singleton.mainHandler.removeCallbacksAndMessages(null);
    }

    public static void check(final int every_N_minutes, final CheckCallback checkCallback) {
        checkInit();
        if (!singleton.isChecking) {
            singleton.subTheadHandler.post(new Runnable() {
                @Override
                public void run() {
                    singleton.isChecking = true;
                    try {
                        String result = P.getUpdataInfo(singleton.url);
                        if (result == null || result.equals("")) {
                            throw new IOException("no result returned!");
                        } else {
                            UpdateInfo updateInfo = UpdateInfo.parse(result);
                            versionCheck(updateInfo, checkCallback);
                        }
                    } catch (Exception e) {
                        checkCallback.onFail(e);
                    } finally {
                        if (every_N_minutes > 0 && singleton.running) {
                            singleton.subTheadHandler.postDelayed(this, every_N_minutes * 60 * 1000);
                        }
                        singleton.isChecking = false;
                    }
                }
            });
        }
    }

    public static void download(String url, DownloadCallback downloadCallback) {
        checkInit();
        if (!singleton.isDownloading) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                }
            };
            singleton.subTheadHandler.post(runnable);
        }
    }

    public static void install() {
        checkInit();
        final File f = getUpdatePath(singleton.mContext);
        if (f == null) return;
        if (!f.exists()) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(f),
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                singleton.mContext.startActivity(intent);
            }
        };
        singleton.mainHandler.post(runnable);
    }

    /*
    * 执行更新的动作
    * 0-----1(min)----2(new)
    * switch(cur){
    *   case [0,1):强制更新; break;
    *   case [1,2):选择更新; break;
    *   case 2:已经是最新; break;
    * }
    * */
    public static void versionCheck(final UpdateInfo updateInfo, final CheckCallback checkCallback) {
        if (updateInfo == null || checkCallback == null) return;
        final int r1 = compareVersion(singleton.curVersion, updateInfo.min_version);
        final int r2 = compareVersion(singleton.curVersion, updateInfo.new_version);
        singleton.mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (r1 < 0) {
                    checkCallback.onMustUpdate(updateInfo);
                } else if (r2 < 0) {
                    checkCallback.onOptUpdate(updateInfo);
                } else {
                    checkCallback.onAlreadyNewest(updateInfo);
                }
            }
        });
    }

    //比较版本信息
    private static int compareVersion(String ver1, String ver2) {
        String[] ver1Array = ver1.split("\\.");
        String[] ver2Array = ver2.split("\\.");
        int shorter = ver1Array.length < ver2Array.length ? ver1Array.length : ver2Array.length;
        for (int i = 0; i < shorter; i++) {
            if (Integer.parseInt(ver1Array[i]) > Integer.parseInt(ver2Array[i])) {
                return 1;
            } else if (Integer.parseInt(ver1Array[i]) < Integer.parseInt(ver2Array[i])) {
                return -1;
            }
        }
        if (ver1Array.length < ver2Array.length) {
            return -1;
        } else if (ver1Array.length > ver2Array.length) {
            return 1;
        }
        return 0;
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

    }
}
