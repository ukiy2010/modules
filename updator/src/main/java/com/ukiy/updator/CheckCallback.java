package com.ukiy.updator;

import android.content.Context;

/**
 * Created by UKIY on 2016/1/8.
 */
public interface CheckCallback {
    //可选更新
    void onOptUpdate(UpdateInfo result);

    //必须更新
    void onMustUpdate(UpdateInfo result);

    //已经是最新
    void onAlreadyNewest(UpdateInfo result);

    //错误
    void onFail(Exception e);
}
