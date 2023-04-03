package com.lripl.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.lripl.database.AppDatabase;
import com.lripl.dealer.ProductListActivity;
import com.lripl.entities.Products;
import com.lripl.entities.States;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductViewmodel extends BaseViewModel {

    private ProductListActivity activity;
    private LiveData<List<Products>> productListLiveData = new MutableLiveData<>();

    private LiveData<List<Products>> updatedProductListLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Products>> filteredProductListLiveData = new MutableLiveData<>();

    private LiveData<List<States>> statesLiveData = new MutableLiveData<>();

    String item_type_id, item_id;

    public String itemName = "";

    ProductViewmodel(AppCompatActivity appCompatActivity) {
        this.activity = (ProductListActivity) appCompatActivity;
        item_type_id = activity.getIntent().getStringExtra(Constants.PARSE_ITEM_TYPE_ID);
        item_id = activity.getIntent().getStringExtra(Constants.PARSE_ITEM_ID);
        itemName = activity.getIntent().getStringExtra(Constants.PARSE_STRING_FEILD);
        statesLiveData = AppDatabase.getInstance(activity.getApplicationContext()).statesDao().getAllStates();

        if (!Utils.NullChecker(item_type_id).isEmpty()) {
            productListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().getProductListByItemTypeId(item_type_id);
        } else {
            productListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().getProductListByItemId(item_id);
        }

    }

    public void getProductListBySearch(String searchValue) {

        if (searchValue.length() > 0) {
            if (!Utils.NullChecker(item_type_id).isEmpty()) {
                updatedProductListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().findProductListByItemTypeId(searchValue, item_type_id);
            } else {
                updatedProductListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().findProductListByItemId(searchValue, item_id);
            }

        } else {
            if (!Utils.NullChecker(item_type_id).isEmpty()) {
                updatedProductListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().getProductListByItemTypeId(item_type_id);
            } else {
                updatedProductListLiveData = AppDatabase.getInstance(activity.getApplicationContext()).productsDao().getProductListByItemId(item_id);
            }

        }
    }

    public void getFilteredProductList(Set<String> filteredStateList, Set<String> filteredBrandList, List<Products> productList) {

        if (filteredStateList.size() > 0 && filteredBrandList.size() > 0) {
            List<Products> stateFilterProductList = filterProductListByState(productList, filteredStateList);
            List<Products> brandFilterProductList = filterProductListByBrand(productList, filteredBrandList);
            Set<Products> uniqueProductLis = new HashSet<>();
            uniqueProductLis.addAll(brandFilterProductList);
            uniqueProductLis.addAll(stateFilterProductList);
            filteredProductListLiveData.postValue(new ArrayList<>(uniqueProductLis));
        } else if (filteredStateList.size() > 0) {
            filteredProductListLiveData.postValue(filterProductListByState(productList, filteredStateList));
        } else if (filteredBrandList.size() > 0) {
            filteredProductListLiveData.postValue(filterProductListByBrand(productList, filteredBrandList));
        }else{
            filteredProductListLiveData.postValue(productList);
        }
    }

    private List<Products> filterProductListByState(List<Products> productList, Set<String> filteredStateList){
        Set<Products> filteredProductList = new HashSet<>();
        for(Products products : productList){
            if(!TextUtils.isEmpty(products.state_id)) {
                //Log.i("ProductListActivity", "view model state id" + products.state_id);
                String[] splitStateList = products.state_id.split(",");
                //Log.i("ProductListActivity", "view model state splitStateList" + splitStateList.length);
                for (String stateId : splitStateList) {
                    if (filteredStateList.contains(stateId)) {
                        filteredProductList.add(products);
                    }
                }
            }
        }
      return new ArrayList<>(filteredProductList);
    }

    private List<Products> filterProductListByBrand(List<Products> productList, Set<String> filteredBrandList){
        Set<Products> filteredProductList = new HashSet<>();
        for(Products products : productList){
            if(!TextUtils.isEmpty(products.brandName)) {
                //Log.i("ProductListActivity", "view model brand name" + products.brandName);
                String[] splitBrandList = products.brandName.split(",");
                //Log.i("ProductListActivity", "view model brand splitBrandList" + splitBrandList.length);
                for (String brand : splitBrandList) {
                    if (filteredBrandList.contains(brand)) {
                        filteredProductList.add(products);
                    }
                }
            }
        }
        return new ArrayList<>(filteredProductList);
    }

    public LiveData<List<States>> getStatesLiveData() {
        return statesLiveData;
    }

    public LiveData<List<Products>> getProductListLiveData() {
        return productListLiveData;
    }

    public LiveData<List<Products>> getFilteredProductListLiveData() {
        return filteredProductListLiveData;
    }

    public LiveData<List<Products>> getSearchedProductListLiveData() {
        return updatedProductListLiveData;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void diplayError(String error) {

    }
}
