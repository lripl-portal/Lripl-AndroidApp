package com.lripl.viewmodels;

import androidx.appcompat.app.AppCompatActivity;

import com.lripl.dealer.EnquirySuccessActivity;

public class EnquirySuccessViewModel extends BaseViewModel {

    private EnquirySuccessActivity activity;
    EnquirySuccessViewModel(AppCompatActivity activity){
        this.activity = (EnquirySuccessActivity) activity;
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
