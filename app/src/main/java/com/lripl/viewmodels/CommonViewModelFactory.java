package com.lripl.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public class CommonViewModelFactory implements ViewModelProvider.Factory {

     private AppCompatActivity appCompatActivity;
     public CommonViewModelFactory(AppCompatActivity appCompatActivity) {
         this.appCompatActivity = appCompatActivity;

    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       if(modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(appCompatActivity);
        }else if(modelClass.isAssignableFrom(OtpViewModel.class)){
           return (T) new OtpViewModel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(ProfileViewModel.class)){
           return (T) new ProfileViewModel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(ItemTypesViewModel.class)){
           return (T) new ItemTypesViewModel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(ItemViewModel.class)){
           return (T) new ItemViewModel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(ProductViewmodel.class)){
            return (T) new ProductViewmodel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(CartListViewModel.class)){
           return (T) new CartListViewModel(appCompatActivity);
       }else if(modelClass.isAssignableFrom(EnquiryListViewModel.class)){
           return (T) new EnquiryListViewModel(appCompatActivity);
       }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
