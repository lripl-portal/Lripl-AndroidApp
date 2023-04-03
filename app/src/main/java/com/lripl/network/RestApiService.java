package com.lripl.network;

import com.lripl.entities.ItemType;
import com.lripl.entities.Orders;
import com.lripl.entities.Users;
import com.lripl.entities.Zones;
import com.lripl.mvp.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    @POST("sendOtp")
    Observable<Response<BaseResponse>> sendOTP(@Body RequestBody body);

    @POST("verifyOtp")
    Observable<Response<BaseResponse>> verifyOTP(@Body RequestBody body);

    @POST("resendOtp")
    Observable<Response<BaseResponse>> resendOTP(@Body RequestBody body);

    @POST("mlogin")
    Observable<Response<Users>> login(@Body RequestBody body);

    @POST("mSaveProfile")
    Observable<Response<Users>> profile(@Header("Authorization") String authToken, @Body RequestBody body);

    @GET("getitemtypes")
    Observable<Response<List<ItemType>>> getitemtypes(@Header("Authorization") String authToken);

    @GET("getZone")
    Observable<Response<List<Zones>>> getzones(@Header("Authorization") String authToken);

    @POST("saveOrders")
    Observable<Response<BaseResponse>> saveorders(@Header("Authorization") String authToken, @Body RequestBody body);

    @GET("getorders")
    Observable<Response<List<Orders>>> getOrders(@Header("Authorization") String authToken, @Query("user_id") String userId);

}
