package com.lripl.viewmodels;

import android.content.ContentValues;

import com.lripl.entities.ItemType;
import com.lripl.entities.Orders;
import com.lripl.entities.Users;
import com.lripl.entities.Zones;
import com.lripl.mvp.BaseResponse;

import java.util.List;

public interface BaseViewInterface {
     void showProgress();
     void hideProgress();
     void diplayError(String error);
     interface LoginPresenterInterface{
          void getSendOTPResponse(String body);
          void sendOTPResponse(BaseResponse response);
     }
     interface OtpPresenterInterface{
          void verifyOtpRequest(String body);
          void verifyOtpResponse(BaseResponse baseResponse);
          void resendOtpRequest(String body);
          void resendOtpResponse(BaseResponse baseResponse);
          void loginUserRequest(String body);
          void loginUserResponse(Users user);
     }
     interface ProfilePresenterInterface{
          void saveUserRequest(String body);
          void saveUserResponse(Users user);
          void loadZonesRequest();
          void loadZonesResponse(List<Zones> zonesList);

     }
     interface ItemTypesInterface{
          void getItemTypesRequest();
          void getItemTypesResponse(List<ItemType> itemTypeList);
     }
     interface CartListInterface{
          void sendOrderRequest(String body);
          void sendOrderResponse(BaseResponse response);

     }

     interface EnquiryListInterface{
          void getOrdersRequest(String userId);
          void getOrdersResponse(List<Orders> ordersList);
     }




}
