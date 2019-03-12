package com.luo.mobile_safe.app;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;

/*
    如果要使用全部功能，需手动激活设备管理器。
*/
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
