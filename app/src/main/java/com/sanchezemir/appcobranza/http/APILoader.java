package com.sanchezemir.appcobranza.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APILoader {

    private static Api api;
    private static APILoader loader;
    private final String URL_BASE = "https://ordenesservicio-api.herokuapp.com/";

    public static Api getApi(){
        if(loader == null) loader = new APILoader();
        return api;
    }

    private APILoader(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);
    }
}
