package com.jarchie.performance.net;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("wxarticle/list/408/1/json")
    Call<ResponseBody> getArticleList();

}