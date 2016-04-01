package com.ukiy.updator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;

/**
 * Created by UKIY on 2016/3/25.
 */
public class Updator {
    private static final String TAG = "Updator";
    private static final int WHAT_CHECK = 1 << 0;
    private static final int WHAT_DOWNLOAD = 1 << 1;
    private static final int WHAT_IDLE = 1 << 2;
    private static volatile Updator singleton;
    private String url;
    private String curVersion;

    private HandlerThread handlerThread = new HandlerThread(TAG);
    private Handler requestHandler = new Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CHECK:
                    if (status == Status.IDLE) {
                        status = Status.CHECKING;
                        //todo doing check
                        P.getUpdataInfo();
                    }
                    break;
                case WHAT_DOWNLOAD:
                    if (status == Status.IDLE) {
                        status = Status.DOWNLOADING;
                        //todo doing download
                    }
                    break;
                case WHAT_IDLE:
                    status = Status.IDLE;
                    break;
                default:
                    throw new RuntimeException("Unknown status");
            }
        }
    };
    private Handler mainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CHECK:
                    if (status == Status.IDLE) {
                        status = Status.CHECKING;
                    }
                    break;
                case WHAT_DOWNLOAD:
                    if (status == Status.IDLE) {
                        status = Status.DOWNLOADING;
                    }
                    break;
                case WHAT_IDLE:
                    status = Status.IDLE;
                    break;
                default:
                    throw new RuntimeException("Unknown status");
            }
        }
    };
    private Status status = Status.IDLE;

    enum Status {
        IDLE, CHECKING, DOWNLOADING
    }

    private Updator(String curVersion, String url) {
        this.url = url;
        this.curVersion = curVersion;
    }

    public static void init(String curVersion, String url) {
        if (singleton != null) {
            throw new RuntimeException("You can init Updator only once!");
        }
        if (singleton == null) {
            synchronized (Updator.class) {
                if (singleton == null)
                    singleton = new Updator(curVersion, url);
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

    public static void check(Context context, long period, CheckCallback checkCallback) {
        checkInit();
//        UpdatorService.check(context, period, checkCallback);
//        singleton.requestHandler.post
        singleton.dispatch(WHAT_CHECK);
    }

    public static void download(Context context, String url, DownloadCallback downloadCallback) {
        checkInit();
//        UpdatorService.download(context, url, downloadCallback);
        singleton.dispatch(WHAT_DOWNLOAD);
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

    }

    private void dispatch(int what) {
        Message message = requestHandler.obtainMessage(what).setData();
        message.sendToTarget();
    }
}
