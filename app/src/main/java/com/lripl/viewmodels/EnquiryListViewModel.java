package com.lripl.viewmodels;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.EnquiriesListActivity;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.R;
import com.lripl.entities.Orders;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EnquiryListViewModel extends BaseViewModel implements BaseViewInterface.EnquiryListInterface{

    private MutableLiveData<List<Orders>> ordersListLivedata = new MutableLiveData<>();

    private EnquiriesListActivity activity;
    EnquiryListViewModel(AppCompatActivity activity){
        this.activity = (EnquiriesListActivity) activity;

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
    public void getOrdersRequest(String userId) {
        showProgress();
        getObservable(userId).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    public MutableLiveData<List<Orders>> getOrdersLiveData(){
        return ordersListLivedata;
    }


    @Override
    public void getOrdersResponse(List<Orders> ordersList) {
        if(ordersList.size() > 0){
            InsertOrders(ordersList);
        }else{
            ordersListLivedata.postValue(ordersList);
        }
    }

    private void InsertOrders(final List<Orders> ordersList){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for(Orders orders:ordersList){
                    AppDatabase.getInstance(activity).ordersDao().insert(orders);
                    AppDatabase.getInstance(activity).orderItemDao().insertAll(orders.orderItemObjs);
                }
                hideProgress();
                ordersListLivedata.postValue(ordersList);
            }
        });

    }

    private Observable<Response<List<Orders>>> getObservable(String body){
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),body);
        return RestApiClient.getRetrofit().create(RestApiService.class).getOrders(SharedPrefsHelper.getInstanse(activity)
                .get(Constants.USER_AUTH_TOKEN, ""), body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<List<Orders>>> getObserver(){
        return new DisposableObserver<Response<List<Orders>>>() {
            @Override
            public void onNext(Response<List<Orders>> baseResponseResponse) {
                if(baseResponseResponse.code() == Constants.HTTP_OK){
                    getOrdersResponse(baseResponseResponse.body());
                }else{
                    diplayError(baseResponseResponse.errorBody().toString());
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
                Intent itemIntent = new Intent(activity, ItemsTypeListActivity.class);
                activity.startActivity(itemIntent);
                activity.finish();
            }
        });

        if(context!=null) {
            alertDialog.show();
        }
    }
}
