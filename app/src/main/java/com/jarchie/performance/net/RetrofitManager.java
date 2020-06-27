package com.jarchie.performance.net;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jarchie.performance.app.BaseApp;
import com.jarchie.performance.constants.Constant;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class RetrofitManager {

    private static final APIService API_SERVICE;

    public static APIService getApiService() {
        return API_SERVICE;
    }


    static {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //文件路径和最大值
        Cache cache = new Cache(BaseApp.getApplication().getCacheDir(),10*1024*1024);
        client.cache(cache).
                eventListenerFactory(OkHttpEventListener.FACTORY).
                dns(OkHttpDNS.getIns(BaseApp.getApplication())).
                addNetworkInterceptor(new StethoInterceptor()).
                addInterceptor(new NoNetInterceptor()).
                addInterceptor(logging);

        final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(Constant.DO_MAIN)
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(client.build())
                .build();
        API_SERVICE = RETROFIT.create(APIService.class);
    }


}
