package com.lripl.viewmodels;

import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.OtpVerificatoinActivity;
import com.lripl.dealer.ProfileActivity;
import com.lripl.dealer.interfaces.OTPEditTextsViewModelInterface;
import com.lripl.entities.Users;
import com.lripl.entities.Zones;
import com.lripl.mvp.BaseResponse;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class OtpViewModel extends BaseViewModel implements BaseViewInterface.OtpPresenterInterface {

    // public MutableLiveData<String> otp_pin = new MutableLiveData<>();
    public MutableLiveData<String> txt_otp_desc = new MutableLiveData<>();

    private OtpVerificatoinActivity activity;

    private OTPEditTextsViewModelInterface mOTPEditTextsViewModelInterface;

    public EditText[] pinCodeEditTexList;

    OtpViewModel(AppCompatActivity appCompatActivity) {
        this.activity = (OtpVerificatoinActivity) appCompatActivity;
        //txt_otp_desc.setValue(activity.getString(R.string.otp_text, "\n"+activity.getIntent().getStringExtra(Constants.PARSE_PHONE_NUMBER)));
    }

    @Override
    public void showProgress() {
        activity.progress_wheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity.progress_wheel.setVisibility(View.GONE);
    }

    public void setPinCodeEditText(EditText[] pinCodeEditText, OTPEditTextsViewModelInterface otpEditTextsViewModelInterface) {
        this.pinCodeEditTexList = pinCodeEditText;
        this.mOTPEditTextsViewModelInterface = otpEditTextsViewModelInterface;
        handleEditTextUIBehaviour();

    }


    private void handleEditTextUIBehaviour() {
        for (int i = 0; i < pinCodeEditTexList.length; i++) {
            final int finalLocalI = i;
            pinCodeEditTexList[finalLocalI].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    OtpViewModel.this.onTextChanged(pinCodeEditTexList[finalLocalI]);
                }
            });

            pinCodeEditTexList[finalLocalI].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL))) {
                        if (pinCodeEditTexList[finalLocalI].length() == 0)
                            requestFocusForEditTextBeforeIndex(finalLocalI);
                    } else if (((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_0))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_1))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_2))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_3))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_4))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_5))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_6))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_7))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_8))
                            || ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_9))) {
                        if (pinCodeEditTexList[finalLocalI].length() == 1)
                            requestFocusForEditTextAfterIndex(finalLocalI);
                    }
                    return false;
                }
            });
        }
    }

    public void onTextChanged(EditText forEditText) {

        for (int i = 0; i < pinCodeEditTexList.length; i++) {
            if (pinCodeEditTexList[i] == forEditText) {
                if (forEditText.length() == 1) {
                    requestFocusForEditTextAfterIndex(i);
                } else if (forEditText.length() == 0) {
                    requestFocusForEditTextBeforeIndex(i);
                }
            }
        }

        mOTPEditTextsViewModelInterface.onOTPEnteringCompleted(otpEntered(), doWehaveAllEdiTextsFilled());

    }

    /**
     * @return Entered OTP only if all fields are entered :: Elese Returns null if any of the OTP field is empty
     */
    public String otpEntered() {
        String otp = null;
        if (doWehaveAllEdiTextsFilled()) {
            otp = "";
            for (EditText editText : pinCodeEditTexList) {
                otp += editText.getText().toString();
            }
        }
        return otp;
    }

    private boolean doWehaveAllEdiTextsFilled() {
        for (int i = 0; i < pinCodeEditTexList.length; i++) {
            if (pinCodeEditTexList[i].length() > 0) {
                if (i == pinCodeEditTexList.length - 1)
                    return true;
            } else break;
        }
        return false;
    }

    private void requestFocusForEditTextAfterIndex(int afterIndex) {
        afterIndex++;
        if (afterIndex < pinCodeEditTexList.length)
            requestFocusForEditTextAtIndex(afterIndex);
    }

    private void requestFocusForEditTextBeforeIndex(int beforeIndex) {
        beforeIndex--;
        if (beforeIndex >= 0)
            requestFocusForEditTextAtIndex(beforeIndex);
    }

    public void requestFocusForEditTextAtIndex(int atIndex) {
        pinCodeEditTexList[atIndex].requestFocus();
        //showKeyboard(pinCodeEditTexList[atIndex]);
    }

    public void showKeyboard(EditText forEditText) {
        Utils.showKeyboard(activity, forEditText);
    }

    public void hideKeyboard(EditText forEditText) {
        Utils.hideKeyboard(activity, (forEditText != null ? forEditText : pinCodeEditTexList[0]));
    }

    public String getValues(String value) {
        try {
            JSONObject object = new JSONObject();
            object.put("phonenumber", activity.getIntent().getStringExtra(Constants.PARSE_PHONE_NUMBER));
            object.put("otp", value);
            return object.toString();
        } catch (Exception e) {
        }
        return null;
    }

    public String getOTPResendValues() {
        try {
            JSONObject object = new JSONObject();
            object.put("phonenumber", activity.getIntent().getStringExtra(Constants.PARSE_PHONE_NUMBER));
            return object.toString();
        } catch (Exception e) {
        }

        return null;
    }

    public String getLoginValues() {
        try {
            JSONObject object = new JSONObject();
            object.put("phonenumber", activity.getIntent().getStringExtra(Constants.PARSE_PHONE_NUMBER));
            object.put("devicetoken", SharedPrefsHelper.getInstance(activity).get(Constants.PREF_KEYS.DEVICE_TOKEN.name(), ""));
            object.put("platform", Constants.PLATFORM);
            object.put("device", Build.MANUFACTURER + " " + Build.MODEL);
            object.put("deviceos", Build.VERSION.RELEASE + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());

            return object.toString();
        } catch (Exception e) {
        }

        return null;
    }


    @Override
    public void diplayError(String error) {
        Utils.showAlertDialog(activity, error);
    }

    @Override
    public void verifyOtpRequest(String body) {
        //loginUserRequest(getLoginValues());
        getObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    private Observable<Response<BaseResponse>> getObservable(String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).verifyOTP(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<BaseResponse>> getObserver() {
        return new DisposableObserver<Response<BaseResponse>>() {
            @Override
            public void onNext(Response<BaseResponse> baseResponseResponse) {
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    verifyOtpResponse(baseResponseResponse.body());
                } else {
                    diplayError(baseResponseResponse.errorBody().toString());
                }
            }

            @Override
            public void onError(Throwable e) {

                diplayError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<Response<BaseResponse>> getResendObservable(String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).resendOTP(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<BaseResponse>> getResendObserver() {
        return new DisposableObserver<Response<BaseResponse>>() {
            @Override
            public void onNext(Response<BaseResponse> baseResponseResponse) {
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    resendOtpResponse(baseResponseResponse.body());
                } else {
                    diplayError(baseResponseResponse.errorBody().toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                diplayError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable<Response<Users>> getLoginObservable(String body) {
        Log.i(OtpViewModel.class.getName(), "Request body-> " + body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).login(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<Users>> getLoginObserver() {
        return new DisposableObserver<Response<Users>>() {
            @Override
            public void onNext(Response<Users> baseResponseResponse) {
                Log.i(OtpViewModel.class.getName(), "response observer" + baseResponseResponse.code());
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    Log.i(OtpViewModel.class.getName(), "response body" + baseResponseResponse.body());
                    loginUserResponse(baseResponseResponse.body());
                } else {
                    diplayError(baseResponseResponse.errorBody().toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                diplayError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void verifyOtpResponse(BaseResponse response) {
        Log.i(OtpViewModel.class.getName(), "---------verifyOtpResponse---------" + response.getserverHttpStatusCode() + " : " + response.getserverStringResponse());
        if (response.getserverHttpStatusCode() == 200) {
            loginUserRequest(getLoginValues());
        } else {
            Utils.showAlertDialog(activity, response.getserverStringResponse());
        }
    }

    @Override
    public void resendOtpRequest(String body) {
        getResendObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getResendObserver());
    }

    @Override
    public void resendOtpResponse(BaseResponse baseResponse) {
        if (baseResponse.getserverHttpStatusCode() == 200) {
            Toast.makeText(activity, "OTP sent successfully...", Toast.LENGTH_LONG).show();
        } else {
            Utils.showAlertDialog(activity, baseResponse.getserverStringResponse());
        }
    }

    @Override
    public void loginUserRequest(String body) {
        getLoginObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getLoginObserver());
    }

    @Override
    public void loginUserResponse(Users user) {
        Log.i(OtpViewModel.class.getName(), "---------loginUserResponse---------" + user + " : " + user.user_id);
        if (user == null) {
            Utils.showAlertDialog(activity, user.phonenumber);
        } else if (Utils.NullChecker(user.user_id).isEmpty()) {
            Utils.showAlertDialog(activity, user.phonenumber);
        } else if (user.isactive) {
            SharedPrefsHelper.getInstance(activity).put(Constants.USER_AUTH_TOKEN, user.token);
            insertuser(user);
            loadZonesRequest();
        } else {
            SharedPrefsHelper.getInstance(activity).put(Constants.USER_AUTH_TOKEN, user.token);
            Intent otp_intent = new Intent(activity, ProfileActivity.class);
            otp_intent.putExtra(Constants.PARSE_USER_OBJ, user);
            otp_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(otp_intent);
            activity.finish();
        }
    }

    private void insertuser(final Users user) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            AppDatabase.getInstance(activity.getApplicationContext()).userDao().insert(user);
            Intent otp_intent = new Intent(activity, ItemsTypeListActivity.class);
            otp_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(otp_intent);
            activity.finish();
        });
    }

    public void onOtpVerifyClick(View view) {
        verifyOtpRequest(getValues(otpEntered()));
    }

    public void onLoginClick(View view) {
        activity.finish();
    }

    public void onResendOtpClick() {
        resendOtpRequest(getOTPResendValues());
    }

    public void onBackClick(View view) {
        activity.finish();
    }

    public void loadZonesRequest() {
        getZonesObservable().retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getZonesObserver());
    }

    private Observable<Response<List<Zones>>> getZonesObservable() {
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),body);
        return RestApiClient.getRetrofit().create(RestApiService.class).getzones(SharedPrefsHelper.getInstance(activity)
                .get(Constants.USER_AUTH_TOKEN, "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public void loadZonesResponse(List<Zones> zonesList) {
        Log.i(ProfileViewModel.class.getName(), "----------loadZonesResponse-------" + zonesList.size());
        if (zonesList != null && zonesList.size() > 0) {
            insertZonesAndStates(zonesList);
        } else {
            Utils.showAlertDialog(activity, "Zones loading failed. Please try again.");
        }
    }


    private void insertZonesAndStates(final List<Zones> zonesList) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            for (Zones zones : zonesList) {
                AppDatabase.getInstance(activity.getApplicationContext()).zonesDao().saveZone(zones);
                Log.i(ProfileViewModel.class.getName(), "----------States Size-------" + zones.statesList.size());
                AppDatabase.getInstance(activity.getApplicationContext()).statesDao().saveStates(zones.statesList);
            }

        });

        //activity.spinnerAdapter.notifyDataSetChanged();
    }

    private DisposableObserver<Response<List<Zones>>> getZonesObserver() {
        return new DisposableObserver<Response<List<Zones>>>() {
            @Override
            public void onNext(Response<List<Zones>> baseResponseResponse) {
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    loadZonesResponse(baseResponseResponse.body());
                } else {
                    diplayError(baseResponseResponse.errorBody().toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                diplayError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }


}
