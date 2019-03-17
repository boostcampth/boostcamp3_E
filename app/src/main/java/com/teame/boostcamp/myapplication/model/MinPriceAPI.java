package com.teame.boostcamp.myapplication.model;

import com.teame.boostcamp.myapplication.model.entitiy.MinPriceResponse;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MinPriceAPI {

    private static MinPriceAPI INSTANCE;
    private static int QUERY_DISPLAY = 1;
    private static int QUERY_START = 1;
    private static String QUERY_SORT = "acs";

    private String baseUrl = "https://openapi.naver.com/";
    public API api;

    private MinPriceAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static MinPriceAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MinPriceAPI();
        }
        return INSTANCE;
    }


    public interface API {
        @GET("v1/search/shop.json?display=1&start=1&sort=asc")
        @Headers({"X-Naver-Client-Id:z9Z80PiumT1gVVUgqs6K", "X-Naver-Client-Secret:7TtoP9_U9U"})
        Observable<MinPriceResponse> getMinPrice(@Query("query") String str);
    }
}