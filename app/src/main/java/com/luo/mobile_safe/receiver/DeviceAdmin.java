package com.luo.mobile_safe.receiver;

import android.annotation.SuppressLint;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceAdmin extends DeviceAdminReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }
}
