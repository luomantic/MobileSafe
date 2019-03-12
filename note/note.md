### 用GithubAPI当服务器地址：
- 获取update-info.json：
[https://raw.githubusercontent.com/luomantic/mobile-safe/master/app/update-info.json](https://raw.githubusercontent.com/luomantic/mobile-safe/master/app/update-info.json)

- 新版本apk下载地址：  
[https://raw.githubusercontent.com/luomantic/mobile-safe/master/app/mobile-safev2.0.apk](https://raw.githubusercontent.com/luomantic/mobile-safe/master/app/mobile-safev2.0.apk)


### retrofit-chat-robot
    public interface GetRequest_Interface {

    	// @GET注解的作用:采用Get方法发送网络请求
    	// getCall() = 接收网络请求数据的方法
    	// 其中返回类型为Call<*>，*是接收数据的类（即上面定义的Translation类）
    	// 如果想直接获得Responsebody中的内容，可以定义网络请求返回值为Call<ResponseBody>
    	@GET("api?key=80aa4127a6c340da8b6841db0edd78c5&info=nihao")
    	Call<Translation> getCall();

    	@GET("api?key=80aa4127a6c340da8b6841db0edd78c5&")
    	Call<Translation> getCall(@Query("info")String info);
    }


    public void request(String askMsg){
        // 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.TULING_API_BASE) // 设置网络请求的基本url
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建网络请求接口的实例
        GetRequest_Interface request_interface = retrofit.create(GetRequest_Interface.class);

        // 对发送请求进行封装
        call = request_interface.getCall(askMsg);

        // 发送网络请求（异步）
        call.enqueue(new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                String answer = response.body().getText();

                // TODO：获取到信息之后，让获取到的text，显示在ListView上
                talkBeans.add(new TalkBean(answer, false, -1));
                adapter.notifyDataSetChanged();
                SpeechSynTools.getInstance().speak(answer);
                listView.setSelection(talkBeans.size() -1);
            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {

            }
        });
    }


### retrofit_mobile-safe
	public interface IRequestServices {

    	@GET("update-info.json")
    	Call<ResponseBody> getString();

    }

    private void checkUpdate() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.UPDATE_URL)
                //.addConverterFactory(GsonConverterFactory.create())
                .build();

        IRequestServices requestServices = retrofit.create(IRequestServices.class);

        Call<ResponseBody> call = requestServices.getString();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    ToastUtils.showShort(result);
                    Log.e("SplashActivity", " result = " + result);

                    // Json解析
                    JSONObject object = new JSONObject(result);
                    String version = (String) object.get("version");
                    String description = (String) object.get("description");
                    String apkurl = (String) object.get("apkurl");
                    Log.e("SplashActivity", version + description + apkurl );

                    if (AppUtils.getAppVersionName().equals(version)) {
                        // 没有更新，进入主页面
                    }else {
                        // 检测到新的更新，弹出更新的对话框
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.showShort("访问网络失败");
            }
        });

    }