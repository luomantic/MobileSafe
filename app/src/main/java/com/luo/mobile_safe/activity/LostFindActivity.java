package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.luo.mobile_safe.Constant;
import com.luo.mobile_safe.R;

public class LostFindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isConfig = SPUtils.getInstance().getBoolean(Constant.IS_CONFIGED);
        if (isConfig) {
            setContentView(R.layout.activity_lost_find);
        }else {
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }
    }
}
