package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.luo.mobile_safe.R;

public class Setup1Activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void next(View view) {
        startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

}
