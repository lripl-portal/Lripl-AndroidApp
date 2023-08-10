package com.lripl.viewmodels;

import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lripl.dealer.LoginActivity;
import com.lripl.dealer.OtpVerificatoinActivity;
import com.lripl.dealer.R;
import com.lripl.mvp.BaseResponse;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class LoginViewModel extends BaseViewModel implements BaseViewInterface.LoginPresenterInterface {
    public MutableLiveData<String> phonenumber = new MutableLiveData<>();
    public MutableLiveData<String> errorValue = new MutableLiveData<>();
    private LoginActivity activity;

    LoginViewModel(AppCompatActivity appCompatActivity) {
        this.activity = (LoginActivity) appCompatActivity;

    }

    @Override
    public void showProgress() {
        activity.progress_wheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity.progress_wheel.setVisibility(View.GONE);
    }

    private String getValues(String value) {
        try {
            JSONObject object = new JSONObject();
            object.put("phonenumber", value);
            return object.toString();
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public void diplayError(String error) {
        Utils.showAlertDialog(activity, error);
        /*Intent otp_intent = new Intent(activity, OtpVerificatoinActivity.class);
        otp_intent.putExtra(Constants.PARSE_PHONE_NUMBER, activity.getResources().getString(R.string.phone_number_suffix)+phonenumber.getValue());
        activity.startActivity(otp_intent);
        activity.finish();*/
    }

    public void onBackClick() {
        activity.finish();
    }

    public void onLoginClick(View view) {
        Utils.hideSoftKeyboard(activity);
        Log.i(LoginActivity.class.getName(), "---------------onClick-------" + activity);
        String phone_number = Utils.NullChecker(phonenumber.getValue()).trim();
        phone_number = phone_number.contains(activity.getString(R.string.phone_number_suffix)) ? phone_number : (activity.getString(R.string.phone_number_suffix) + phone_number);

        if (Utils.NullChecker(phone_number).isEmpty()) {
            Utils.showAlertDialog(activity, activity.getResources().getString(R.string.phone_number_1_validation));
        } else if (phone_number.length() != 13) {
            Utils.showAlertDialog(activity, activity.getResources().getString(R.string.phone_number_2_validation));
        } else {

            getSendOTPResponse(getValues(phone_number));
            /*Intent otp_intent = new Intent(activity, OtpVerificatoinActivity.class);
            otp_intent.putExtra(Constants.PARSE_PHONE_NUMBER, activity.getResources().getString(R.string.phone_number_suffix)+phonenumber.getValue());
            activity.startActivity(otp_intent);
            activity.finish();*/
        }
    }

    @Override
    public void getSendOTPResponse(String body) {
        showProgress();
        getObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    @Override
    public void sendOTPResponse(BaseResponse response) {
        if (response.getserverHttpStatusCode() == 200) {
            Intent otp_intent = new Intent(activity, OtpVerificatoinActivity.class);
            otp_intent.putExtra(Constants.PARSE_PHONE_NUMBER, activity.getResources().getString(R.string.phone_number_suffix) + phonenumber.getValue());
            activity.startActivityForResult(otp_intent, Constants.LOGIN_RESULT);
            //activity.finish();
        } else {
            Utils.showAlertDialog(activity, response.getserverStringResponse());
        }
    }

    private Observable<Response<BaseResponse>> getObservable(String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).sendOTP(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<BaseResponse>> getObserver() {
        return new DisposableObserver<Response<BaseResponse>>() {
            @Override
            public void onNext(Response<BaseResponse> baseResponseResponse) {
                hideProgress();
                Log.i(LoginViewModel.class.getName(), "-----------LoginPresenter---" + baseResponseResponse.code() + " : " + baseResponseResponse.isSuccessful() + " : " + baseResponseResponse.message());
                try {
                    if (baseResponseResponse.code() == Constants.HTTP_OK) {
                        sendOTPResponse(baseResponseResponse.body());
                    } else {
                        diplayError(baseResponseResponse.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                hideProgress();
                diplayError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                hideProgress();
            }
        };
    }


}

