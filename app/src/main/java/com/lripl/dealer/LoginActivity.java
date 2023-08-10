package com.lripl.dealer;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lripl.dealer.databinding.LoginLayoutBinding;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int SMS_PERMISSION_REQ_CODE_SUBMIT = 101;
    LoginViewModel loginViewModel;
    LoginLayoutBinding loginLayoutBinding;
    CommonViewModelFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.login_layout);
        // loginLayoutBinding = DataBindingUtil.setContentView(this,R.layout.login_layout);
        factory = new CommonViewModelFactory(this);
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        loginLayoutBinding.setLoginViewModel(loginViewModel);
        loginLayoutBinding.setLifecycleOwner(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    SMS_PERMISSION_REQ_CODE_SUBMIT);
        }


        loginLayoutBinding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(charSequence.length() == 10){
                        Utils.hideSoftKeyboard(LoginActivity.this);
                    }
                loginLayoutBinding.btnSendOtp.setEnabled(charSequence.length() == 10);
                loginLayoutBinding.btnSendOtp.setTextColor(charSequence.length() == 10 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.semi_dark_grey));
                loginLayoutBinding.btnSendOtp.setBackgroundResource(charSequence.length() == 10 ? R.drawable.verify_btn_bg_selector : R.drawable.negative_btn_bg_selector);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.onBackClick();
            }
        });
    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        txtTitle.setText(getString(R.string.txt_log_in));
        loginLayoutBinding = DataBindingUtil.bind(view);
        cartLayout.setVisibility(View.INVISIBLE);

    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(LoginActivity.class.getName(), "---------------onRequestPermissionsResult-------" + requestCode + " : " + permissions[0]);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(LoginActivity.class.getName(), "---------------onActivityResult-------" + resultCode + " : " + requestCode);
        if (requestCode == Constants.LOGIN_RESULT) {
            loginLayoutBinding.edtPhoneNumber.setText("");
            /*Intent i = new Intent(LoginActivity.this, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();*/
        }
    }


}


