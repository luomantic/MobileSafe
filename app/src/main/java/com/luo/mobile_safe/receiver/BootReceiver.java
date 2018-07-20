package com.luo.mobile_safe.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.luo.mobile_safe.constant.Constant;

public class BootReceiver extends BroadcastReceiver {

    String newNumber;
    String oldNumber;

    @SuppressLint("HardwareIds")
    @Override
    public void onReceive(Context context, Intent intent) {
        // 一旦监听到开机广播，就需要去发送短信，给指定号码
        Log.e("tag", "接收到了开机广播...");

        oldNumber = SPUtils.getInstance().getString(Constant.SIM_NUMBER);
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (manager != null) {
                newNumber = manager.getSimSerialNumber();
            }
        }

        if (!StringUtils.equals(oldNumber, newNumber)) {
            SmsManager smsManager = SmsManager.getDefault();
            String desNumber = SPUtils.getInstance().getString(Constant.CONTACT_NUM);
            smsManager.sendTextMessage(desNumber, null, "SIM Changed!!!", null, null);
        }

    }

}
