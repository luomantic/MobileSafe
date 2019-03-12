package com.luo.mobile_safe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    SettingItemView siv_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        siv_update = findViewById(R.id.siv_update);

        boolean open_update = SPUtils.getInstance().getBoolean(Constant.OPEN_UPDATE);
        siv_update.setChecked(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_update.isChecked();
                siv_update.setChecked(!isCheck);

                SPUtils.getInstance().put(Constant.OPEN_UPDATE, siv_update.isChecked());
            }
        });
    }

}
