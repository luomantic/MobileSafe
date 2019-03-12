package com.luo.mobile_safe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.service.LocationService;

// 未测试，在系统action中不提示。有些机型还需要对系统短信优先级，进行设置。
public class SmsReceiver extends BroadcastReceiver {
    ComponentName componentName;
    DevicePolicyManager mDPM;

    @Override
    public void onReceive(Context context, Intent intent) {
        //1,判断是否开启了防盗保护
        boolean open_security = SPUtils.getInstance().getBoolean(Constant.OPEN_SECURITY);
        if(open_security){
            //2,获取短信内容
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //3,循环遍历短信过程
            for (Object object : objects) {
                //4,获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
                //5,获取短信对象的基本信息
                // sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                //6,判断是否包含播放音乐的关键字
                if(messageBody.contains("#*alarm*#")){
                    //7,播放音乐(准备音乐,MediaPlayer)
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }

                if(messageBody.contains("#*location*#")){
                    //8,开启获取位置服务
                    context.startService(new Intent(context,LocationService.class));
                }

                componentName = new ComponentName(context, DeviceAdmin.class);
                mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                if(messageBody.contains("#*lockscrenn*#")){
                    if (mDPM.isAdminActive(componentName)) {
                        mDPM.lockNow();
                        mDPM.resetPassword("123", 0);
                    }else {
                        ToastUtils.showLong("请先激活");
                    }
                }
                if(messageBody.contains("#*wipedate*#")){
                    if (mDPM.isAdminActive(componentName)) {
                        mDPM.wipeData(0); // 手机数据
                        //mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE); // 手机SD卡数据
                        // 还有Camera的数据等等，具体看官方文档
                    }else {
                        ToastUtils.showLong("请先激活");
                    }
                }
            }
        }
    }

}
