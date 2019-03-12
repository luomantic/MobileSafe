package com.luo.mobile_safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;

public class SettingItemView extends RelativeLayout {
    CheckBox checkBox;
    TextView tv_title;
    TextView tv_des;

    String mDesOn;
    String mDesOff;
    String mDesTitle;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);

        tv_title = this.findViewById(R.id.tv_title);
        tv_des = this.findViewById(R.id.tv_des);
        checkBox = this.findViewById(R.id.cb_box);

        initAttrs(attrs);
        tv_title.setText(mDesTitle);
    }

    private void initAttrs(AttributeSet attrs) {
        /*
        for (int i=0; i<attrs.getAttributeCount(); i++) {
            Log.e("tag", "name：" + attrs.getAttributeName(i));
            Log.e("tag", "value：" + attrs.getAttributeValue(i));
            Log.e("tag", "------------------------------------------" );
        }
        */
        mDesTitle = attrs.getAttributeValue(Constant.NAMESPACE, "desTitle");
        mDesOff = attrs.getAttributeValue(Constant.NAMESPACE, "desOff");
        mDesOn = attrs.getAttributeValue(Constant.NAMESPACE, "desOn");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /*
       返回当前 SettingItemView 是否选中状态 true开启（checkBox返回true） false关闭（checkBox反回false）
    */
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public void setChecked(boolean isChecked) {
        checkBox.setChecked(isChecked); // checkBox的状态也会跟着变化
        if (isChecked) {
            tv_des.setText(mDesOn);
        }else {
            tv_des.setText(mDesOff);
        }
    }
}
