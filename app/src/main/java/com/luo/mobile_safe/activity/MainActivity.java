package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;

/*
重写手机安全卫士，做代码保留
*/
public class MainActivity extends AppCompatActivity {

    MaterialDialog dialog;
    GridView gridView;
    private static String[] names;
    private static int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        names = new String[]{
                "手机防盗", "通讯卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"
        };
        ids = new int[]{
                R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app,
                R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan,
                R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings
        };
        gridView.setAdapter(new GridAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        /*
                            密码设置Dialog的两种实现模式：
                            ① 正常的MaterialDialog设置input属性，直接设置两次，要求两次密码相同，才设置成功。
                            ② 给MaterialDialog设置自定义布局，然后在布局里面进行密码设置操作。
                        */
                        showPasswordDialog();
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, AToolActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }

    private void showPasswordDialog() {
        String password = SPUtils.getInstance().getString(Constant.PASSWORD);

        if (StringUtils.isEmpty(password)) {
            showSetPasswordDialog();
        } else {
            showConfirmPasswordDialog();
        }
        dialog.show();
    }

    private void showConfirmPasswordDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title("登录密码")
                .titleGravity(GravityEnum.CENTER)
                .buttonsGravity(GravityEnum.CENTER)
                .content("请输入您设置的密码")
                .input("确认密码", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .cancelable(false)
                .positiveText("确定")
                .negativeText("取消")
                .build();
        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.getInputEditText() != null) {
                    String number = dialog.getInputEditText().getText().toString();
                    if (StringUtils.equals(EncryptUtils.encryptMD5ToString(number), SPUtils.getInstance().getString(Constant.PASSWORD))) {
                        startActivity(new Intent(MainActivity.this, LostFindActivity.class));
                    }else {
                        ToastUtils.showLong("密码错误");
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showSetPasswordDialog() {
        dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_password, true)
                .cancelable(false)
                .build();

        View customView = dialog.getCustomView();

        if (customView!=null) {
            final EditText et_set = customView.findViewById(R.id.et_set_psd);
            final EditText et_confirm = customView.findViewById(R.id.et_confirm_psd);
            Button bt_submit = customView.findViewById(R.id.bt_submit);
            Button bt_cancel = customView.findViewById(R.id.bt_cancel);

            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd1 = et_confirm.getText().toString();
                    String pwd2 = et_set.getText().toString();
                    String password = EncryptUtils.encryptMD5ToString(pwd1);

                    if (StringUtils.equals(pwd1, pwd2)) {
                        SPUtils.getInstance().put(Constant.PASSWORD, password);
                        ToastUtils.showLong("密码设置成功");
                    } else {
                        ToastUtils.showLong("设置密码失败");
                    }
                    dialog.dismiss();
                }
            });
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void initView() {
        gridView = findViewById(R.id.grid_home);
    }

    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = View.inflate(MainActivity.this, R.layout.grid_item_main, null);
                holder.imageView = view.findViewById(R.id.iv_item);
                holder.textView = view.findViewById(R.id.tv_item);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.textView.setText(names[i]);
            holder.imageView.setImageResource(ids[i]);

            return view;
        }
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
}
