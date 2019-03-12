package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.constant.Constant;

public class Setup4Activity extends BaseSetupActivity {
    CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initView();
    }

    @Override
    public void showNext() {
        if (SPUtils.getInstance().getBoolean(Constant.OPEN_SECURITY)) {
            startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
            finish();
            SPUtils.getInstance().put(Constant.IS_CONFIG, true);
        } else {
            ToastUtils.showLong("请开启防盗保护设置");
        }

        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    public void showPre() {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initView() {
        checkBox = findViewById(R.id.cb_box);
        boolean open_security = SPUtils.getInstance().getBoolean(Constant.OPEN_SECURITY);
        checkBox.setChecked(open_security);
        if (open_security) {
            checkBox.setText("安全设置已开启");
        }else {
            checkBox.setText("安全设置已关闭");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.getInstance().put(Constant.OPEN_SECURITY, isChecked);
                if (isChecked) {
                    checkBox.setText("安全设置已开启");
                }else {
                    checkBox.setText("安全设置已关闭");
                }
            }
        });
    }

}
