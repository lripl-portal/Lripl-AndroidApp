package com.lripl.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
