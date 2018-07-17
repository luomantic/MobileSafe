package com.luo.mobile_safe.app;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;

public class MobileApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FileDownloader.setup(this);
        Utils.init(this);
        initToast();
    }

    private void initToast() {
        ToastUtils.setMsgTextSize(20);
    }

}
