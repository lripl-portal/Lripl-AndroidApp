package com.lripl.viewmodels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.CartListActivity;
import com.lripl.dealer.EnquirySuccessActivity;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.R;
import com.lripl.mvp.BaseResponse;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CartListViewModel extends BaseViewModel implements BaseViewInterface.CartListInterface {

    private CartListActivity activity;
    CartListViewModel(AppCompatActivity activity){
        this.activity = (CartListActivity) activity;
    }

    @Override
    public void showProgress() {
        activity.progress_wheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity.progress_wheel.setVisibility(View.GONE);
    }

    @Override
    public void diplayError(String error) {
        Utils.showAlertDialog(activity,error);
    }

    @Override
    public void sendOrderRequest(String body) {
        showProgress();
        getObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    @Override
    public void sendOrderResponse(BaseResponse response) {
        try{
            if(response.getserverHttpStatusCode() == 200){
                Utils.product_enquiry.clear();
                activity.enquiry_livedata.setValue(Utils.product_enquiry);
                Intent enquirySuccess = new Intent(activity, EnquirySuccessActivity.class);
                activity.startActivity(enquirySuccess);
                activity.finish();
               // showAlertDialog(activity,response.getserverStringResponse());
            }else{
                Utils.showAlertDialog(activity, activity.getString(R.string.network_error));
            }
        }catch (Exception e){

        }
    }

    private Observable<Response<BaseResponse>> getObservable(String body){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),body);
        return RestApiClient.getRetrofit().create(RestApiService.class).saveorders(SharedPrefsHelper.getInstanse(activity)
                .get(Constants.USER_AUTH_TOKEN, ""), requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<BaseResponse>> getObserver(){
        return new DisposableObserver<Response<BaseResponse>>() {
            @Override
            public void onNext(Response<BaseResponse> baseResponseResponse) {
                hideProgress();
                if(baseResponseResponse.code() == Constants.HTTP_OK){
                    sendOrderResponse(baseResponseResponse.body());
                }else{
                    diplayError(activity.getString(R.string.network_error));
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

    private void showAlertDialog(Context context, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);
        alertDialog.setButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });

        if(context!=null) {
            alertDialog.show();
        }
    }
}

