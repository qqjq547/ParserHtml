package com.hjq.parserhtml.http.retrofit;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient2 {
    private static ApiClient2 instances;
    private ApiStores apiStores;
    private ApiStores apiStores1;
    public static String API_SERVER_URL = "http://www.lesmao.com/";
    public static OkHttpClient okHttpClient;

    public static Retrofit Retrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置 Debug Log 模式
        builder.addInterceptor(loggingInterceptor);


        //设置超时
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        });
        okHttpClient = builder.build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return mRetrofit;
    }
    public static Retrofit Retrofit_ForSessionId() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        //设置超时
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        });
        OkHttpClient okHttpClient = builder.build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(API_SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return mRetrofit;
    }


    public static ApiClient2 getInstance() {
        if (instances == null) {
            instances = new ApiClient2();
        }
        return instances;
    }
    public static ApiClient2 getInstance1() {
        if (instances == null) {
            instances = new ApiClient2();
        }
        return instances;
    }

    private ApiClient2() {
        apiStores = Retrofit(API_SERVER_URL).create(ApiStores.class);
        apiStores1 = Retrofit_ForSessionId().create(ApiStores.class);
    }


    public ApiStores getApiStores() {
        return apiStores;
    }
    public ApiStores getApiStores1() {
        return apiStores1;
    }


}
