package com.luo.mobile_safe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/*
自动获取焦点的TextView
*/
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {

    // 通过java代码创建控件
    public MarqueeTextView(Context context) {
        super(context);
    }

    // 通过xml布局文件创建控件，由系统调用
    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // 带属性，上下文环境，以及Style文件中的样式的构造方法
    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
