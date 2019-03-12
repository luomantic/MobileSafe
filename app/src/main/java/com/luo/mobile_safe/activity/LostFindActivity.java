package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;

import org.w3c.dom.Text;

// 手机防盗页面
public class LostFindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isConfig = SPUtils.getInstance().getBoolean(Constant.IS_CONFIG);

        if (isConfig) {
            setContentView(R.layout.activity_lost_find);
            initView();
        }else {
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }

    }

    private void initView() {
        TextView tv_reset_setup = findViewById(R.id.tv_reset_setup);
        TextView tv_safe_number = findViewById(R.id.tv_safe_number);

        tv_safe_number.setText(SPUtils.getInstance().getString(Constant.CONTACT_NUM)); // TextView默认没有点击事件
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
                finish();
            }
        });

    }
}
