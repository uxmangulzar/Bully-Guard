package com.example.codesteembullyguard.network;


import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit = null;
    public static String BASE_URL = "bully-gaurd/";

    public Retrofit getClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES);

        // Add cookie jar for handling cookies
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        clientBuilder.addInterceptor(loggingInterceptor);
//        clientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                okhttp3.Response response = chain.proceed(chain.request());
//                String contentType = response.header("Content-Type");
//                if (contentType != null && contentType.contains("text/html")) {
//                    // Handle HTML response as plain string
//                    return response.newBuilder()
//                            .body(ResponseBody.create(response.body().contentType(), response.body().string()))
//                            .build();
//                } else {
//                    return response;
//                }
//            }
//        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}




