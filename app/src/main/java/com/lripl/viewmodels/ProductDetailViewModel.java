package com.lripl.viewmodels;

import androidx.appcompat.app.AppCompatActivity;

import com.lripl.dealer.ProductDetailActivity;

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
