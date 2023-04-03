package com.lripl.dealer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lripl.adapters.CategoryAdapter;
import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.dealer.databinding.ItemtypesLayoutBinding;
import com.lripl.entities.ItemType;
import com.lripl.entities.Orders;
import com.lripl.entities.Users;
import com.lripl.utils.Constants;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.ItemTypesViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemsTypeListActivity extends  BaseActivity implements CategoryAdapter.ItemClickListener {

    ItemtypesLayoutBinding itemtypesLayoutBinding;
    ItemTypesViewModel itemTypesViewModel;
    CommonViewModelFactory factory;
    CategoryAdapter productCategoryAdapter;
    public Orders recentOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.itemtypes_layout);
        factory = new CommonViewModelFactory(this);
        itemTypesViewModel = ViewModelProviders.of(this,factory).get(ItemTypesViewModel.class);
        itemtypesLayoutBinding.setItemTypeViewModel(itemTypesViewModel);
        itemtypesLayoutBinding.setLifecycleOwner(this);
        itemtypesLayoutBinding.gridLayout.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =  new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.horizontal_line_shape));
        itemtypesLayoutBinding.gridLayout.addItemDecoration(dividerItemDecoration);

        productCategoryAdapter = new CategoryAdapter(this,itemTypesViewModel.getItemListLiveData().getValue());
        productCategoryAdapter.setClickListener(this);
        itemtypesLayoutBinding.gridLayout.setAdapter(productCategoryAdapter);

        itemTypesViewModel.getItemListLiveData().observe(this, new Observer<List<ItemType>>() {
            @Override
            public void onChanged(@Nullable List<ItemType> itemTypeList) {
                productCategoryAdapter.setData(itemTypeList);
            }
        });

        AppDatabase.getInstance(this).userDao().getUserEntity().observe(this, new Observer<Users>() {
            @Override
            public void onChanged(@Nullable Users users) {
                if(users != null) {
                    if(!TextUtils.isEmpty(users.firstname) && ! TextUtils.isEmpty(users.lastname)) {
                        itemtypesLayoutBinding.txtUserName.setText("Welcome, " + users.firstname + " " + users.lastname);
                    }else if(!TextUtils.isEmpty(users.firstname) && TextUtils.isEmpty(users.lastname)){
                        itemtypesLayoutBinding.txtUserName.setText("Welcome, " + users.firstname);
                    }
                    else if(TextUtils.isEmpty(users.firstname) && !TextUtils.isEmpty(users.lastname)){
                        itemtypesLayoutBinding.txtUserName.setText("Welcome, " + users.lastname);
                    }else if(!TextUtils.isEmpty(users.fullname)){
                        itemtypesLayoutBinding.txtUserName.setText("Welcome, " + users.fullname);
                    }else{
                        itemtypesLayoutBinding.txtUserName.setText("Welcome, Guest");
                    }
                    setUserNameText(itemtypesLayoutBinding.txtUserName.getText().toString());
                    itemTypesViewModel.getOrdersRequest(users.user_id);
                }else{
                    itemtypesLayoutBinding.txtUserName.setText("Welcome, Guest");

                }
            }
        });



        itemTypesViewModel.getOrdersLiveData().observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(@Nullable List<Orders> ordersList) {
              Log.i("ItemsTypeListActivity","on order changed "+ordersList.size());
              if(ordersList != null && ordersList.size() > 0){
                  Collections.sort(ordersList, new Comparator<Orders>() {
                      @Override
                      public int compare(Orders o1, Orders o2) {
                          return o2.createdat.compareTo(o1.createdat);
                      }
                  });
                  itemtypesLayoutBinding.btnViewEnquiry.setVisibility(View.VISIBLE);
                  recentOrder = ordersList.get(0);
              itemtypesLayoutBinding.txtEnquiryNo.setText("Recent Enquiry No "+recentOrder.order_number);
              }else{
                  itemtypesLayoutBinding.btnViewEnquiry.setVisibility(View.GONE);
                  itemtypesLayoutBinding.txtEnquiryNo.setText("You have not enquired yet.");
              }

            }
        });

    }

    public void getLoacalOrders(final String usr_id){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Orders> ordersList = AppDatabase.getInstance(ItemsTypeListActivity.this).ordersDao().getAllOrdersByuser(usr_id);
                for(int i=0;i<ordersList.size();i++ ){
                    ordersList.get(i).orderItemObjs = AppDatabase.getInstance(ItemsTypeListActivity.this).orderItemDao().getAllOrderItemsByorder(ordersList.get(i).order_id);
                }
                Collections.sort(ordersList, new Comparator<Orders>() {
                    @Override
                    public int compare(Orders o1, Orders o2) {
                        return o1.createdat.compareTo(o2.createdat);
                    }
                });
                itemTypesViewModel.getOrdersLiveData().postValue(ordersList);
            }
        });
    }

    private void setUserNameText(String userName){
        Log.i("ItemsTypeListActivity", "userName length>> "+userName.length());
        if(userName.length() >= 25 && userName.length() < 30){
            itemtypesLayoutBinding.txtUserName.setTextSize(16);
        }else if(userName.length() >=30 && userName.length() < 40){
            itemtypesLayoutBinding.txtUserName.setTextSize(14);
        }else if(userName.length() >=40 ){
            itemtypesLayoutBinding.txtUserName.setTextSize(12);
        }
    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        txtTitle.setText(getString(R.string.products_list));
        itemtypesLayoutBinding = DataBindingUtil.bind(view);
        iconBack.setVisibility(View.INVISIBLE);
        iconMenu.setVisibility(View.VISIBLE);
        iconNotification.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                i = new Intent(ItemsTypeListActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_enquiries:
                i = new Intent(ItemsTypeListActivity.this, EnquiriesListActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_contactus:
                Intent help = new Intent(ItemsTypeListActivity.this, HelpActivity.class);
                startActivity(help);
                break;
            case R.id.nav_about_us:
                Intent aboutUs = new Intent(ItemsTypeListActivity.this, AboutUsActivity.class);
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

    @Override
    public void onItemClick(View view, final int position) {
        LiveData<Integer> itemscount = AppDatabase.getInstance(ItemsTypeListActivity.this.getApplicationContext()).itemsDao().getItemsCount(productCategoryAdapter.getItemType(position).item_type_id);
        //itemscount.observe(ItemsTypeListActivity.this,itemscountobserver);

        itemscount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer > 0){
                    Intent i = new Intent(ItemsTypeListActivity.this, ItemsListActivity.class);
                    i.putExtra(Constants.PARSE_ITEM_TYPE_ID,productCategoryAdapter.getItemType(position).item_type_id);
                    i.putExtra(Constants.PARSE_STRING_FEILD,productCategoryAdapter.getItemType(position).name);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Intent i = new Intent(ItemsTypeListActivity.this, ProductListActivity.class);
                    i.putExtra(Constants.PARSE_ITEM_TYPE_ID,productCategoryAdapter.getItemType(position).item_type_id);
                    i.putExtra(Constants.PARSE_STRING_FEILD,productCategoryAdapter.getItemType(position).name);
                    if (productCategoryAdapter.getItemType(position).name.contains("XC Series") || productCategoryAdapter.getItemType(position).name.contains("Premium Series")) {
                        i.putExtra(Constants.IS_BRAND_FILTER_DISPLAY, true);
                    }
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


}
