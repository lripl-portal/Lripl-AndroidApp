package com.lripl.dealer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.lripl.adapters.CartListAdapter;
import com.lripl.dealer.databinding.CartListLayoutBinding;
import com.lripl.entities.Products;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CartListViewModel;
import com.lripl.viewmodels.CommonViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.internal.Util;

public class CartListActivity extends BaseActivity implements CartListAdapter.CartItemDeleteListener{

    CartListLayoutBinding cartListLayoutBinding;
    CartListAdapter cartListAdapter;
    CartListViewModel cartListViewModel;
    CommonViewModelFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.cart_list_layout);

        factory = new CommonViewModelFactory(this);
        cartListViewModel = ViewModelProviders.of(this,factory).get(CartListViewModel.class);
        cartListLayoutBinding.setCartListViewModel(cartListViewModel);
        cartListLayoutBinding.setLifecycleOwner(this);


        if(Utils.product_enquiry != null && Utils.product_enquiry.size() > 0) {
            cartListLayoutBinding.cartListLayout.setVisibility(View.VISIBLE);
            cartListLayoutBinding.cardEmptyLayout.setVisibility(View.GONE);
            cartListLayoutBinding.gridLayout.setLayoutManager(new LinearLayoutManager(this));
            cartListAdapter = new CartListAdapter(this, Utils.product_enquiry);
            cartListAdapter.setClickListener(this);
            cartListLayoutBinding.gridLayout.setAdapter(cartListAdapter);
        }else{
            cartListLayoutBinding.cartListLayout.setVisibility(View.GONE);
            cartListLayoutBinding.cardEmptyLayout.setVisibility(View.VISIBLE);
        }


        cartListLayoutBinding.btnBackMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(CartListActivity.this, ItemsTypeListActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

        cartListLayoutBinding.btnSendEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent enquirySuccess = new Intent(CartListActivity.this, EnquirySuccessActivity.class);
                startActivity(enquirySuccess);*/
               if(isQuantityMissed()){
                    Utils.showAlertDialog(CartListActivity.this,"Please enter the quantity for all products.");
                }else{
                    cartListViewModel.sendOrderRequest(getRequestBody());
                }

            }
        });

    }
    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        //toolbar.setVisibility(View.VISIBLE);
        txtTitle.setText(getString(R.string.cart_list_screen_name));
        cartListLayoutBinding = DataBindingUtil.bind(view);
        iconNotification.setVisibility(View.INVISIBLE);
        cartLayout.setVisibility(View.INVISIBLE);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private String getRequestBody(){
        JSONObject jsonObject = new JSONObject();
        try{
            JSONObject orderJson = new JSONObject();
            orderJson.put("user_id",user_id);
            orderJson.put("status", Constants.ORDER_STATUS);
            //orderJson.put("order_number", "");
            JSONArray orderItemArray = new JSONArray();
            for(Products products : Utils.product_enquiry){
                JSONObject productObj = new JSONObject();
                productObj.put("user_id",user_id);
                productObj.put("product_id",products.product_id);
                productObj.put("product_name",products.name);
                productObj.put("product_image",products.imageurl);
                productObj.put("quantity",products.quantity);
                productObj.put("status",Constants.ORDER_STATUS);
                orderItemArray.put(productObj);
            }
            orderJson.put("orderItemObjs", orderItemArray);
            jsonObject.put("Order",orderJson);


        }catch (Exception e){

        }
        return jsonObject.toString();
    }

    private boolean isQuantityMissed(){
        boolean isquantitymiss = false;
        for (Products products : Utils.product_enquiry){
            if (products.quantity == 0){
                isquantitymiss = true;
                break;
            }
        }
        return isquantitymiss;
    }

    @Override
    public void onItemDeleteClick(View view, int position) {
        Utils.product_enquiry.remove(position);
        enquiry_livedata.setValue(Utils.product_enquiry);
        if(Utils.product_enquiry != null && Utils.product_enquiry.size() > 0) {
            if (cartListAdapter != null) {
                cartListAdapter.notifyDataSetChanged();
            }
        }else{
            cartListLayoutBinding.cartListLayout.setVisibility(View.GONE);
            cartListLayoutBinding.cardEmptyLayout.setVisibility(View.VISIBLE);
        }
    }
}
