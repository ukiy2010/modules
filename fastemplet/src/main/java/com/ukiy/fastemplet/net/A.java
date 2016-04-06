package com.ukiy.fastemplet.net;

import com.ukiy.fastemplet.beans.Bean;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by UKIY on 2016/4/6.
 */
public class A {
    public static final String BASE_URL = "https://api.github.com";

    public static final MyApiEndpointInterface pi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MyApiEndpointInterface.class);

    public interface MyApiEndpointInterface {
        @GET("/users/{username}")
        Call<Bean> test2(@Path("username") String username);

        @POST("/users/new")
        Call<Bean> test1(@Body Bean bean);
    }
}
