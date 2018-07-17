package com.luo.mobile_safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.luo.mobile_safe.constant.Constant;
import com.luo.mobile_safe.R;
import com.luo.mobile_safe.net.IRequestServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int JSON_ERROR = 2;
    private static final int NETWORK_ERROR = 3;

    String version;
    String description;
    String apk_url;

    MaterialDialog dialog;
    NumberProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        boolean checkUpdate = SPUtils.getInstance().getBoolean(Constant.OPEN_UPDATE);
        if (checkUpdate) {
            checkUpdate();
        } else {
            handler.sendEmptyMessageDelayed(ENTER_HOME, 3000);
        }

        startAnimation();
    }

    /* splash页进入动画 */
    private void startAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(alphaAnimation);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == SHOW_UPDATE_DIALOG) {
                showUpdateDialog();
            } else if (message.what == ENTER_HOME) {
                enterHome();
            } else if (message.what == NETWORK_ERROR) {
                enterHome();
                ToastUtils.showShort("网络异常");
            } else if (message.what == JSON_ERROR) {
                enterHome();
                ToastUtils.showShort("Json解析错误");
            }
            return false;
        }
    });

    private void showUpdateDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title("提示升级")
                .content(description)
                .cancelable(false)
                .buttonsGravity(GravityEnum.CENTER)
                .positiveText("立即升级")
                .negativeText("稍后再说")
                .show();
        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAPK();
                dialog.dismiss();
            }
        });
        dialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterHome();
                dialog.dismiss();
            }
        });
    }

    private void downloadAPK() {
        String fileName = Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
        final String path = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;

        progressBar.setVisibility(View.VISIBLE);

        FileDownloader.getImpl()
                .create(Constant.DOWNLOAD_URL)
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        float progress = (float)soFarBytes/totalBytes *  100;
                        progressBar.setProgress((int) progress + 1);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        AppUtils.installApp(path);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

    private void enterHome() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void checkUpdate() {

        final Message message = Message.obtain();
        final long startTime = System.currentTimeMillis();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.UPDATE_URL)
                .build();

        IRequestServices requestServices = retrofit.create(IRequestServices.class);
        Call<ResponseBody> call = requestServices.getUpdateJson();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    String result;
                    result = Objects.requireNonNull(response.body()).string();
                    Log.e("SplashActivity", " result = " + result);

                    // Json解析
                    JSONObject object = new JSONObject(result);
                    version = (String) object.get("version");
                    description = (String) object.get("description");
                    apk_url = (String) object.get("apkurl");

                    if (AppUtils.getAppVersionName().equals(version)) {
                        // 没有更新，进入主页面
                        message.what = ENTER_HOME;
                    } else {
                        // 检测到新的更新，弹出更新的对话框
                        message.what = SHOW_UPDATE_DIALOG;
                    }
                } catch (JSONException e) {
                    message.what = JSON_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    message.what = NETWORK_ERROR;
                    e.printStackTrace();
                } finally {
                    long lastTime = System.currentTimeMillis();
                    long dTime = lastTime - startTime;
                    if (dTime < 3000) {
                        try {
                            Thread.sleep(3000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ToastUtils.showShort("访问网络失败");
            }
        });

    }

    private void initView() {
        TextView textView = findViewById(R.id.tv_splash_version);
        textView.setText(String.format("版本号：%s", AppUtils.getAppVersionName()));
        progressBar = findViewById(R.id.splash_progress_bar);
        progressBar.setReachedBarHeight(20);
        progressBar.setUnreachedBarHeight(15);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
