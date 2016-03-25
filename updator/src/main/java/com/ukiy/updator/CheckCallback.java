package com.ukiy.updator;

import android.content.Context;

/**
 * Created by UKIY on 2016/1/8.
 */
public interface CheckCallback {
    //可选更新
    void onOptUpdate(Context context, UpdateInfo updateInfo, String cur_version);

    //必须更新
    void onMustUpdate(Context context, UpdateInfo updateInfo, String cur_version);

    //已经是最新
    void onAlreadyNewest(Context context, UpdateInfo updateInfo, String cur_version);

    //占位，暂无实现
    void onFail(Context context);
}
