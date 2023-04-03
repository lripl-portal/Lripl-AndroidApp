package com.lripl.dealer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lripl.dealer.databinding.EnquirySuccessLayoutBinding;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.EnquirySuccessViewModel;

public class EnquirySuccessActivity extends BaseActivity {

    EnquirySuccessLayoutBinding enquirySuccessLayoutBinding;
    EnquirySuccessViewModel enquirySuccessViewModel;
    CommonViewModelFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.enquiry_success_layout);

        enquirySuccessLayoutBinding.imgEnquirySuccess.setGifImageResource(R.drawable.tick);

        enquirySuccessLayoutBinding.btnCategoryMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(EnquirySuccessActivity.this, ItemsTypeListActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

        enquirySuccessLayoutBinding.btnViewEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enquiryList = new Intent(EnquirySuccessActivity.this, EnquiriesListActivity.class);
                enquiryList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(enquiryList);
                finish();
            }
        });

    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        txtTitle.setText(getString(R.string.enquiry_success_screen_name));
        enquirySuccessLayoutBinding = DataBindingUtil.bind(view);
        iconNotification.setVisibility(View.INVISIBLE);
        cartLayout.setVisibility(View.INVISIBLE);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(EnquirySuccessActivity.this, ItemsTypeListActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
