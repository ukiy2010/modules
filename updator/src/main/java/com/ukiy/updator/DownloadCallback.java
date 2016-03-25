package com.ukiy.updator;

import android.app.Service;

/**
 * Created by ukiy on 16/3/12.
 */
public interface DownloadCallback {
    void onProgress(Service service, int progress);

    void onDone(Service service);

    //占位，暂无实现
    void onFail(Service service);
}
