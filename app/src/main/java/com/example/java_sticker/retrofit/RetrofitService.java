package com.example.java_sticker.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;
    // server 의 url 을 적어준다
    private static final String URL = "http://192.168.219.103:9090";

    public RetrofitService() {
        InitializeRetrofit();
    }

    private void InitializeRetrofit() {
        // Retrofit 객체 초기화
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


}

