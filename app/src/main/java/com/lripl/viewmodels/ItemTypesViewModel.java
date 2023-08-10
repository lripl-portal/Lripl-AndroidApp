package com.lripl.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.EnquiriesListActivity;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.R;
import com.lripl.entities.ItemType;
import com.lripl.entities.Orders;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ItemTypesViewModel extends BaseViewModel implements BaseViewInterface.ItemTypesInterface {

    private ItemsTypeListActivity activity;
    private LiveData<List<ItemType>> itemListLiveData = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private MutableLiveData<List<Orders>> ordersListLivedata = new MutableLiveData<>();

    ItemTypesViewModel(AppCompatActivity appCompatActivity){
       this.activity = (ItemsTypeListActivity) appCompatActivity;
       appDatabase = AppDatabase.getInstance(activity);
       fillItemTypes();
       getItemTypesRequest();
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
    public void getItemTypesRequest() {
        getItemTypeObservable().retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getItemTypeObserver());
    }

    @Override
    public void getItemTypesResponse(List<ItemType> itemTypeList) {
        Log.i(ItemTypesViewModel.class.getName(),"-----getItemTypesResponse------"+itemTypeList.size());
        if(itemTypeList != null && itemTypeList.size() > 0){
           insertItemsIntoDB(itemTypeList);
        }else {
            diplayError(activity.getResources().getString(R.string.network_error));
        }
    }

    private void insertItemsIntoDB(final List<ItemType> itemTypeList){
        AppExecutors.getInstance().diskIO().execute(() -> {
            for (ItemType itemType : itemTypeList){
                Log.i(ItemTypesViewModel.class.getName(),"-----getItemTypesResponse------"+itemType.name+" : "+itemType.itemslist.size());
                appDatabase.itemTypeDao().saveItemType(itemType);
                appDatabase.itemsDao().saveItems(itemType.itemslist);
                appDatabase.productsDao().saveProducts(itemType.productsList);
            }


        });
    }

    private void fillItemTypes(){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                itemListLiveData = appDatabase.itemTypeDao().getAllItemTypes();
                Log.i(ItemTypesViewModel.class.getName(),"-----fillItemTypes------"+itemListLiveData.getValue());
                itemListLiveData.observe(activity, new Observer<List<ItemType>>() {
                    @Override
                    public void onChanged(@Nullable List<ItemType> itemTypeList) {

                        Log.i(ItemTypesViewModel.class.getName(),"-----fillItemTypes observe------"+itemListLiveData.getValue()+" : "+itemTypeList);
                    }
                });
            }
        });
    }

   public LiveData<List<ItemType>> getItemListLiveData(){
        return itemListLiveData;
   }
    private Observable<Response<List<ItemType>>> getItemTypeObservable() {
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).getitemtypes(SharedPrefsHelper.getInstance(activity)
                .get(Constants.USER_AUTH_TOKEN, "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public void onViewEnquiryClick(){

        if(activity.recentOrder != null){
            Intent viewOrder = new Intent(activity, EnquiriesListActivity.class);
            viewOrder.putExtra(Constants.PRODUCT_HOME_SCREEN, true);
            activity.startActivity(viewOrder);
        }

    }

    public void getOrdersRequest(String userId) {
        showProgress();
        getObservable(userId).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    public MutableLiveData<List<Orders>> getOrdersLiveData(){
        return ordersListLivedata;
    }


    public void getOrdersResponse(List<Orders> ordersList) {
        if(ordersList.size() > 0){
            InsertOrders(ordersList);
        }
    }

    private void InsertOrders(final List<Orders> ordersList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Orders orders : ordersList) {
                    AppDatabase.getInstance(activity).ordersDao().insert(orders);
                    AppDatabase.getInstance(activity).orderItemDao().insertAll(orders.orderItemObjs);
                }
                hideProgress();
                ordersListLivedata.postValue(ordersList);
            }
        });
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



    private Observable<Response<List<Orders>>> getObservable(String body){
        Log.i("ViewModelItem","body"+body);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),body);
        return RestApiClient.getRetrofit().create(RestApiService.class).getOrders(SharedPrefsHelper.getInstance(activity)
                .get(Constants.USER_AUTH_TOKEN, ""), body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<List<ItemType>>> getItemTypeObserver() {
        return new DisposableObserver<Response<List<ItemType>>>() {
            @Override
            public void onNext(Response<List<ItemType>> baseResponseResponse) {
                Log.i(ItemTypesViewModel.class.getName(), "-----------LoginPresenter---" + baseResponseResponse.code() + " : " + baseResponseResponse.isSuccessful() + " : " + baseResponseResponse.message());
                try {
                    if (baseResponseResponse.code() == Constants.HTTP_OK) {
                        getItemTypesResponse(baseResponseResponse.body());
                    } else {
                        diplayError(baseResponseResponse.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
