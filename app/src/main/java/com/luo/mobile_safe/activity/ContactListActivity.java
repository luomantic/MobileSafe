package com.luo.mobile_safe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.luo.mobile_safe.R;

public class ContactListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
        initData();
    }

    // 获取系统联系人数据
    private void initData() {
        // ① 通过ContentProvider,查询系统数据库获取联系人。原生方法。
        // ② 使用GreenDao对数据库进行操作，增删改查demo。
        // ③ 找一个获取系统联系人，然后按照字母排序联系人的demo，分析实现。
    }

    private void initView() {
        listView = findViewById(R.id.lv_contact);
    }

}
