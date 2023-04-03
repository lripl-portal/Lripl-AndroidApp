package com.lripl.viewmodels;

import android.support.v7.app.AppCompatActivity;

import com.lripl.database.AppDatabase;
import com.lripl.dealer.ProductDetailActivity;
import com.lripl.dealer.ProductListActivity;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

public class ProductDetailViewModel extends BaseViewModel {

    private ProductDetailActivity activity;

    ProductDetailViewModel(AppCompatActivity appCompatActivity) {
        this.activity = (ProductDetailActivity) appCompatActivity;

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
