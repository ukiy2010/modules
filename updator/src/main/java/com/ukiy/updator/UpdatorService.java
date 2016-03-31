package com.ukiy.updator;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.ViewUtils;
import android.util.TimeUtils;
import android.view.GestureDetector;

/**
 * Created by UKIY on 2016/3/25.
 */
public class UpdatorService extends Service {
    private int status;
    private UpdatorThread ut;

    @Override
    public void onCreate() {
        super.onCreate();
        ut = new UpdatorThread();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UpdatorBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void check(final Context context, long period, CheckCallback checkCallback) {
        Intent intent = new Intent(context, UpdatorService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                UpdatorService updatorService = ((UpdatorBinder) service).getService();

                context.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }

    public static void download(final Context context, final String url, final DownloadCallback downloadCallback) {
        Intent intent = new Intent(context, UpdatorService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                UpdatorService updatorService = ((UpdatorBinder) service).getService();
                P.downoadFile(false, url, , downloadCallback, );
                context.unbindService(this);
                
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }

    private static class UpdatorThread extends HandlerThread {
        private Handler handler;

        public UpdatorThread() {
            super("UpdatorThread");
        }

        @Override
        protected void onLooperPrepared() {
            handler = new Handler();
        }

        public Handler getHandler() {
            return handler;
        }

        public void post(Runnable runnable) {
            if (handler == null) return;
            handler.post(runnable);
        }

        @Override
        public boolean quit() {
            if (handler == null) return super.quit();
            handler.removeCallbacksAndMessages(null);
            return super.quit();
        }
    }

    private class UpdatorBinder extends Binder {
        UpdatorService getService() {
            return UpdatorService.this;
        }
    }
}
