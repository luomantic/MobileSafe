package com.luo.mobile_safe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends AppCompatActivity {

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 3. 创建手势识别器的对象
        gestureDetector = new GestureDetector(this, new  GestureDetector.SimpleOnGestureListener(){

            // 4. 重写手势识别器中，包含按下点和抬起点在移动过程中的方法
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // e1: 起始点      e2: 抬起点
                if (e1.getRawX()-e2.getRawX()>100) {
                    // 下一页，由右向左滑动
                    showNext();
                }
                if (e2.getRawX()-e1.getRawX()>100) {
                    // 上一页，由左向右滑动
                    showPre();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    // 1. 监听当前activity上的触摸事件（按下1次，滑动多次，抬起1次）
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //. 通过手势识别器，去识别不同的事件类型，做逻辑
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void showNext();

    public abstract void showPre();

    public void next(View view){
        showNext();
    }

    public void pre(View view){
        showPre();
    }
}
