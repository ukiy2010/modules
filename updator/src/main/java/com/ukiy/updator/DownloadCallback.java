package com.ukiy.updator;

import java.io.File;

/**
 * Created by ukiy on 16/3/12.
 */
public interface DownloadCallback {
    //返回true表示终止下载
    boolean onProgress(int progress);

    void onDone(File file);

    void onFail();
}
