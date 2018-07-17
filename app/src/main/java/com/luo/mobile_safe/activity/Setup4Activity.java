package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;

public class Setup4Activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void pre(View view) {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();
    }

    public void next(View view) {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        finish();
        SPUtils.getInstance().put(Constant.IS_CONFIG, true);
    }

}
