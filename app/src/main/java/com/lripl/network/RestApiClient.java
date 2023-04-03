package com.lripl.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lripl.dealer.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {
    //private static final String SERVICE_IP = "192.168.1.102"; //49.207.1.139
    private static final String BASE_URL = BuildConfig.BASE_URL;//"http://49.207.1.139:8085/lripl/api/";
    public static final String IMAGES_PATH = BuildConfig.IMAGES_URL;//"http://49.207.1.139:8085/lripl/";
    public static final String PROFILE_IMAGES_PATH = BuildConfig.PROFILE_IMAGES_URL;
    private static Retrofit retrofit;

    public RestApiClient() {

    }

    public static Retrofit getRetrofit() {
        Log.i("Retrofilt***", "Base URL " + BASE_URL);
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //OkHttpClient okHttpClient = builder.build();
            Gson gson = new GsonBuilder().setDateFormat("MMMM dd, yyyy, hh:mm:ss a").create();
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(100000, TimeUnit.MILLISECONDS).readTimeout(100000, TimeUnit.MILLISECONDS)
                    .writeTimeout(100000, TimeUnit.MILLISECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "")
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

}
