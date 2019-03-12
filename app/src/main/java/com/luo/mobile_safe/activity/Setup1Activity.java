package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.luo.mobile_safe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    public void showPre() {

    }

}
