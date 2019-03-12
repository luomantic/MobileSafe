package com.luo.mobile_safe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.luo.mobile_safe.R;
import com.luo.mobile_safe.engine.AddressDao;

public class QueryAddressActivity extends AppCompatActivity {
    EditText et_phone;
    Button bt_query;
    TextView tv_query_result;

    String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        initView();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tv_query_result.setText(address);
            return false;
        }
    });

    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        bt_query = findViewById(R.id.bt_query);
        tv_query_result = findViewById(R.id.tv_query_result);

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_phone.getText().toString())){
                    queryPhone();
                }else {
                    // 抖动
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(shake);

                    /*
                    interpolator插补器,数学函数

                    //自定义插补器
					shake.setInterpolator(new Interpolator() {
						//y = 2x+1
						@Override
						public float getInterpolation(float arg0) {
							return 0;
						}
					});

					Interpolator
					CycleInterpolator
					*/
                }
            }
        });

        // 实时查询
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryPhone();
            }
        });
    }

    private void queryPhone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String number = et_phone.getText().toString();
                address = AddressDao.getAddress(number);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

}
