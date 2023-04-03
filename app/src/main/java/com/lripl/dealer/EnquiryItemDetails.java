package com.lripl.dealer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.lripl.adapters.EnquiryItemsListAdapter;
import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.dealer.databinding.OrderItemDetailsBinding;
import com.lripl.entities.OrderItem;
import com.lripl.entities.Orders;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;

import java.util.List;

public class EnquiryItemDetails extends BaseActivity implements EnquiryItemsListAdapter.ItemClickListener {

    OrderItemDetailsBinding orderItemDetailsBinding;
    EnquiryItemsListAdapter enquiryItemsListAdapter;
    MutableLiveData<List<OrderItem>> orderitemsList =  new MutableLiveData<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.order_item_details);
        final Orders order = (Orders) getIntent().getSerializableExtra(Constants.PARSE_ORDER_ID);
        setUI(order);
        orderItemDetailsBinding.orderItemsList.setLayoutManager(new LinearLayoutManager(this));
        orderitemsList.observe(this, new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(@Nullable List<OrderItem> orderItemList) {
               enquiryItemsListAdapter = new EnquiryItemsListAdapter(EnquiryItemDetails.this,orderItemList);
               orderItemDetailsBinding.orderItemsList.setAdapter(enquiryItemsListAdapter);
            }
        });


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<OrderItem> itemsList = AppDatabase.getInstance(EnquiryItemDetails.this).orderItemDao().getAllOrderItemsByorder(order.order_id);
                orderitemsList.postValue(itemsList);
            }
        });



    }
    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        orderItemDetailsBinding = DataBindingUtil.bind(view);
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


    @Override
    public void onItemClick(View view, int position) {

    }

    private void setUI(Orders or){
        txtTitle.setText(or.order_number);
        orderItemDetailsBinding.txtOrderNumber.setText(or.order_number);
        orderItemDetailsBinding.txtOrderCreatedDate.setText(Utils.getDateFormatWithMonth(or.createdat));
        orderItemDetailsBinding.txtOrderProductsCount.setText(or.orderItemObjs.size()+"");
        orderItemDetailsBinding.txtOrderStatus.setText(or.status);

    }
}
