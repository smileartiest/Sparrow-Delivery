package com.smile.atozdelivery.retrofit;

public class ApiUtil {

    public static final String BASE_URL = "https://sparrowhypermarket.000webhostapp.com/";

    public static RetrofitInterface getServiceClass() {
        return RetrofitAPI.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }
}
