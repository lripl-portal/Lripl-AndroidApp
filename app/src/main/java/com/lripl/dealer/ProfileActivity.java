package com.lripl.dealer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lripl.adapters.SpinnerAdapter;
import com.lripl.customviews.spinner.SelectorAdapter;
import com.lripl.dealer.databinding.ProfileLayoutBinding;
import com.lripl.entities.States;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.ProfileViewModel;
import com.mvc.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity  implements View.OnClickListener{

    public ProfileLayoutBinding profileLayoutBinding;
    CommonViewModelFactory factory;
    ProfileViewModel profileViewModel;

    public Bitmap bitmap;

    public List<States> statesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.profile_layout);
        Utils.hideSoftKeyboard(this);
        factory = new CommonViewModelFactory(this);
        profileViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel.class);
        profileLayoutBinding.setProfileViewModel(profileViewModel);
        profileLayoutBinding.setLifecycleOwner(this);

        //profileLayoutBinding.spnrState.setAdapter(spinnerAdapter);
        //profileLayoutBinding.profileImage.setOnClickListener(this);

        ImagePicker.setMinQuality(600, 600);

        profileViewModel.getStateListLiveData().observe(this, new Observer<List<States>>() {
            @Override
            public void onChanged(@Nullable List<States> states) {
                statesList = states;
                if(states.size() == 0){
                    profileViewModel.loadZonesRequest();
                }

            }
        });

   }

   private SpannableStringBuilder mandatoryFieldText(String hint){
       final SpannableStringBuilder hintText = new SpannableStringBuilder();
       final int start = hint.indexOf("*");
       final int end = start + 1;
       hintText.append(hint);
       hintText.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       hintText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       return hintText;
   }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        //toolbar.setVisibility(View.VISIBLE);
        profileLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(getString(R.string.menu_profile));
        iconBack.setVisibility(View.INVISIBLE);
        iconMenu.setVisibility(View.VISIBLE);
        iconNotification.setVisibility(View.INVISIBLE);
        cartLayout.setVisibility(View.INVISIBLE);
        txtSave.setVisibility(View.VISIBLE);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileViewModel.onSaveClick(view);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.nav_products:
                Intent i = new Intent(ProfileActivity.this, ItemsTypeListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.nav_enquiries:
                i = new Intent(ProfileActivity.this, EnquiriesListActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_contactus:
                Intent help = new Intent(ProfileActivity.this, HelpActivity.class);
                startActivity(help);
                break;
            case R.id.nav_about_us:
                Intent aboutUs = new Intent(ProfileActivity.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.nav_logout:
                displayLogoutAlert(this);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.profile_image:
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    MyApplication.requestWriteExternalStoragePermission(this);
                } else if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    MyApplication.requestCameraPermission(this);
                } else {
                    ImagePicker.pickImage(this, "Select your image:");
                }
                break;

            default:
                break;
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == MyApplication.REQUEST_WRITE_EXTERNAL_STORAGE) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            MyApplication.requestCameraPermission(this);
        } else {
            ImagePicker.pickImage(this, "Select your image:");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        // TODO do something with the bitmap
        if(bitmap != null) {
            profileLayoutBinding.profileImage.setImageBitmap(bitmap);
            Log.i(ProfileActivity.class.getName(), "--------onActivityResult------" + bitmap);
        }

    }


    public void onResume(){
        super.onResume();
        Utils.hideSoftKeyboard(this);
    }
}
