package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.constant.Constant;

public class Setup3Activity extends AppCompatActivity{
    Button button;
    EditText et_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initView();
    }

    private void initView() {
        et_phone_number = findViewById(R.id.et_phone_number);
        button = findViewById(R.id.bt_select_number);

        String phone = SPUtils.getInstance().getString(Constant.CONTACT_NUM);
        if (!StringUtils.isEmpty(phone)) {
            et_phone_number.setText(phone);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Setup3Activity.this, ContactListActivity.class), 0);
            }
        });
    }

    public void pre(View view) {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
    }

    public void next(View view) {
        String phone = et_phone_number.getText().toString();
        if (!StringUtils.isEmpty(phone)) {
            startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
            finish();

            SPUtils.getInstance().put(Constant.CONTACT_NUM, phone);
        } else {
            ToastUtils.showLong("请输入电话号码");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) { // 防止Setup3Activity界面，按回退健data返回为空，程序崩溃的问题。
            // 1. 返回当前界面，接受结果的方法
            String phone = data.getStringExtra("phone");
            // 2. 将特殊字符过滤掉，再显示到界面
            phone = phone.replace("-","").replace(" ", "").trim();
            et_phone_number.setText(phone);
            // 3. 存储联系人至sp中
            SPUtils.getInstance().put(Constant.CONTACT_NUM, phone);
        }
    }
}
