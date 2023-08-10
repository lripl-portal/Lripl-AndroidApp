package com.lripl.dealer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.lripl.adapters.EnquiryListAdapter;
import com.lripl.database.AppDatabase;
import com.lripl.dealer.databinding.EnquiriesListLayoutBinding;
import com.lripl.entities.Orders;
import com.lripl.entities.Users;
import com.lripl.utils.Constants;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.EnquiryListViewModel;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EnquiriesListActivity extends BaseActivity{

    EnquiriesListLayoutBinding enquiriesListLayoutBinding;
    CommonViewModelFactory factory;
    EnquiryListViewModel enquiryListViewModel;
    EnquiryListAdapter enquiryListAdapter;
    int lastExpandedGroupPosition = -1;
    List<Orders> mOrdersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.enquiries_list_layout);
        factory = new CommonViewModelFactory(this);
        enquiryListViewModel = ViewModelProviders.of(this,factory).get(EnquiryListViewModel.class);
        enquiriesListLayoutBinding.setEnquiryViewModel(enquiryListViewModel);
        enquiriesListLayoutBinding.setLifecycleOwner(this);




        AppDatabase.getInstance(this).userDao().getUserEntity().observe(this, new Observer<Users>() {
            @Override
            public void onChanged(@Nullable Users users) {
                //getLoacalOrders(users.user_id);
                if(users != null) {
                    enquiryListViewModel.getOrdersRequest(users.user_id);
                }else{
                    enquiriesListLayoutBinding.enquiryNotFoundLayout.setVisibility(View.VISIBLE);
                    enquiriesListLayoutBinding.ordersList.setVisibility(View.GONE);
                }
            }
        });

        enquiryListViewModel.getOrdersLiveData().observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(@Nullable List<Orders> ordersList) {
                if (ordersList != null && ordersList.size() > 0) {
                    mOrdersList = ordersList;
                    Collections.sort(ordersList, new Comparator<Orders>() {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                        @Override
                        public int compare(Orders o1, Orders o2) {
                            return o2.createdat.compareTo(o1.createdat);
                        }
                    });
                    enquiryListAdapter = new EnquiryListAdapter(EnquiriesListActivity.this, ordersList, enquiriesListLayoutBinding.ordersList);
                    enquiriesListLayoutBinding.ordersList.setAdapter(enquiryListAdapter);
                    if (getIntent().getBooleanExtra(Constants.PRODUCT_HOME_SCREEN, false)) {
                        enquiriesListLayoutBinding.ordersList.expandGroup(0);
                    }
                    //enquiryListAdapter.setData(ordersList);
                }else{
                    enquiriesListLayoutBinding.enquiryNotFoundLayout.setVisibility(View.VISIBLE);
                    enquiriesListLayoutBinding.ordersList.setVisibility(View.GONE);
                }
            }
        });


        enquiriesListLayoutBinding.ordersList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.i("EnquiryList","onGroupCollapse"+groupPosition);
                if(mOrdersList != null && mOrdersList.size() > 0 && groupPosition != -1){
                    mOrdersList.get(groupPosition).isGroupExpanded = false;
                    enquiryListAdapter.notifyDataSetChanged();
                }
            }
        });

        enquiriesListLayoutBinding.ordersList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != lastExpandedGroupPosition) {
                    enquiriesListLayoutBinding.ordersList.collapseGroup(lastExpandedGroupPosition);
                    if(mOrdersList != null && mOrdersList.size() > 0){
                        mOrdersList.get(groupPosition).isGroupExpanded = true;
                        if(lastExpandedGroupPosition != -1) {
                            mOrdersList.get(lastExpandedGroupPosition).isGroupExpanded = false;
                        }
                    }
                    lastExpandedGroupPosition = groupPosition;

                }
                enquiryListAdapter.notifyDataSetChanged();



            }
        });

        enquiriesListLayoutBinding.btnStartEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(EnquiriesListActivity.this, ItemsTypeListActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        enquiriesListLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(getString(R.string.enquiry_details_screen));
        iconBack.setVisibility(View.INVISIBLE);
        iconMenu.setVisibility(View.VISIBLE);
        iconNotification.setVisibility(View.INVISIBLE);
        cartLayout.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                i = new Intent(EnquiriesListActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_products:
                i = new Intent(EnquiriesListActivity.this, ItemsTypeListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.nav_contactus:
                Intent help = new Intent(EnquiriesListActivity.this, HelpActivity.class);
                startActivity(help);
                break;
            case R.id.nav_about_us:
                Intent aboutUs = new Intent(EnquiriesListActivity.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.nav_logout:
                displayLogoutAlert(this);
                break;
            default:
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(EnquiriesListActivity.this,EnquiryItemDetails.class);
        i.putExtra(Constants.PARSE_ORDER_ID,enquiryListAdapter.getOrder(position));
        startActivity(i);
    }*/
}
