package com.luo.mobile_safe.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IRequestServices {

    @GET("update-info.json")
    Call<ResponseBody> getUpdateJson();

}
