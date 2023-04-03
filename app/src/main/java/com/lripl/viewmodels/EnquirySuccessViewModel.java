package com.lripl.viewmodels;

import android.support.v7.app.AppCompatActivity;

import com.lripl.dealer.CartListActivity;
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
