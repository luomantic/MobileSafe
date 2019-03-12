package com.luo.mobile_safe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.luo.mobile_safe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    ListView listView;
    List<HashMap<String, String>> contactList = new ArrayList<>();
    ContactListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
        initData();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 8. 填充数据适配器
            listAdapter = new ContactListAdapter();
            listView.setAdapter(listAdapter);
            return false;
        }
    });

    /*
     获取系统联系人数据:
        ③ 找一个获取系统联系人，然后按照字母排序联系人的demo，分析实现。
        ② 直接打开系统联系人，然后返回数据。
        ③ 通过ContentProvider,查询系统数据库获取联系人信息，然后展示到listView中。
        另外使用GreenDao对数据库进行操作，增删改查demo。
    */
    private void initData() {
        /*
        content://com.android.contacts/data/
        content://com.android.contacts/raw_contacts/
        */

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1. 获取内容解析器
                ContentResolver contentResolver = getContentResolver();
                // 2. 查询系统联系人数据库表的过程。（读取联系人权限）
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts/"),
                        new String[]{"contact_id"},
                        null,
                        null,
                        null
                );
                contactList.clear();
                // 3. 循环游标，知道没有数据为止。
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(0);
                        // 4. 根据用户唯一性id值，查询data表和mimetype表生成的视图，获取data以及mimetype字段
                        Cursor cursor2 = contentResolver.query(
                                Uri.parse("content://com.android.contacts/data/"),
                                new String[]{"data1", "mimetype"},
                                "raw_contact_id = ?",
                                new String[]{id},
                                null);
                        if (cursor2 != null) {
                            // 5. 循环获取每一个联系人的电话号码以及姓名
                            HashMap<String, String> hashMap = new HashMap<>();
                            while (cursor2.moveToNext()) {
                                String data = cursor2.getString(0);
                                String type = cursor2.getString(1);
                                // 6. 区分类型去给HashMap填充数据
                                if (type.equals("vnd.android.cursor.item/phone_v2")) {
                                    hashMap.put("phone", data);
                                }
                                if (type.equals("vnd.android.cursor.item/name")) {
                                    hashMap.put("name", data);
                                }
                            }
                            cursor2.close();
                            contactList.add(hashMap);
                        }
                    }
                    cursor.close();
                    // 7. 消息机制，发送一个空的消息，告诉主线程，可以去使用子线程已经填充好的数据集合
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();

    }

    private void initView() {
        listView = findViewById(R.id.lv_contact);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ①. 获取点中条目的索引指向集合中的对象
                if (listAdapter != null) {
                    HashMap<String, String> hashMap = listAdapter.getItem(position);
                    // ②. 获取当前条目，指向集合对应的电话号码
                    String phone = hashMap.get("phone");
                    // ③. 将此电话号码，put到intent里面，让前一个界面调用
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);

                    finish();
                }
            }
        });
    }

    private class ContactListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ContactListActivity.this, R.layout.list_contact_item, null);
                holder.tv_phone = convertView.findViewById(R.id.tv_phone);
                holder.tv_name = convertView.findViewById(R.id.tv_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(getItem(position).get("name"));
            holder.tv_phone.setText(getItem(position).get("phone"));

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_phone;
    }
}
