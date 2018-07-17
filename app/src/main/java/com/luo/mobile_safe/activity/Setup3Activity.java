package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.Utils;
import com.luo.mobile_safe.R;

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
        startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
