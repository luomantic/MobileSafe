package com.luo.mobile_safe.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {
    SettingItemView itemView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initView();
    }

    private void initView() {
        itemView = findViewById(R.id.siv_sim_bind);
        // 回显
        String sim_number = SPUtils.getInstance().getString(Constant.SIM_NUMBER);
        if (StringUtils.isEmpty(sim_number)) {
            itemView.setChecked(false);
        } else {
            itemView.setChecked(true);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                boolean is_checked = itemView.isChecked();
                itemView.setChecked(!is_checked);
                if (!is_checked) {

                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(Setup2Activity.this,
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        PermissionUtils.launchAppDetailsSettings();
                    }
                    if (manager != null) {
                        @SuppressLint("HardwareIds") String simSerialNumber = manager.getSimSerialNumber();
                        SPUtils.getInstance().put(Constant.SIM_NUMBER, simSerialNumber);
                    }
                }else {
                    SPUtils.getInstance().remove(Constant.SIM_NUMBER);
                }
            }
        });
    }

    public void pre(View view) {
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();
    }

    public void next(View view) {
        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constant.SIM_NUMBER))){
            startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
            finish();
        }else {
            ToastUtils.showLong("请绑定SIM卡");
        }
    }

}
