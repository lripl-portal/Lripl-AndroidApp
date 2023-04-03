package com.lripl.dealer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.lripl.adapters.ItemsGridAdapter;
import com.lripl.database.AppDatabase;
import com.lripl.dealer.databinding.ItemLayoutBinding;
import com.lripl.entities.Items;
import com.lripl.utils.Constants;
import com.lripl.utils.GridSpacingItemDecoration;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.ItemViewModel;

import java.util.List;

public class ItemsListActivity extends BaseActivity implements ItemsGridAdapter.ItemClickListener {

    ItemLayoutBinding itemLayoutBinding;
    CommonViewModelFactory factory;
    ItemViewModel itemViewModel;
    ItemsGridAdapter itemsGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.item_layout);
        factory = new CommonViewModelFactory(this);
        itemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);
        itemLayoutBinding.setItemModel(itemViewModel);
        itemLayoutBinding.setLifecycleOwner(this);
        itemLayoutBinding.gridLayout.setLayoutManager(new GridLayoutManager(this, 3));
        itemLayoutBinding.gridLayout.addItemDecoration(new GridSpacingItemDecoration(3, 25, true));

        DividerItemDecoration horizontalDividerItemDecoration =  new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        //horizontalDividerItemDecoration.setDrawable(getDrawable(R.drawable.horizontal_line_shape));
        //itemLayoutBinding.gridLayout.addItemDecoration(horizontalDividerItemDecoration);

        DividerItemDecoration verticalDividerItemDecoration =  new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //verticalDividerItemDecoration.setDrawable(getDrawable(R.drawable.horizontal_line_shape));
        //itemLayoutBinding.gridLayout.addItemDecoration(verticalDividerItemDecoration);''


        itemsGridAdapter = new ItemsGridAdapter(this);
        itemsGridAdapter.setClickListener(this);
        itemLayoutBinding.gridLayout.setAdapter(itemsGridAdapter);

        itemViewModel.getItemsListLiveData().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(@Nullable List<Items> items) {
                itemsGridAdapter.setData(items);
            }
        });

    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        itemLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(getIntent().getStringExtra(Constants.PARSE_STRING_FEILD));
        iconBack.setVisibility(View.VISIBLE);
        iconMenu.setVisibility(View.INVISIBLE);
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
        drawer.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Intent i = new Intent(ItemsListActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, final int position) {
        LiveData<Integer> productscount = AppDatabase.getInstance(ItemsListActivity.this.getApplicationContext()).productsDao().getProductsCountByItemId(itemsGridAdapter.getItem(position).item_id);
        //itemscount.observe(ItemsTypeListActivity.this,itemscountobserver);

        productscount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                    Intent i = new Intent(ItemsListActivity.this, ProductListActivity.class);
                    i.putExtra(Constants.PARSE_ITEM_ID, itemsGridAdapter.getItem(position).item_id);
                    i.putExtra(Constants.PARSE_STRING_FEILD, itemsGridAdapter.getItem(position).name);
                    if (itemsGridAdapter.getItem(position).name.contains("XC Series") || itemsGridAdapter.getItem(position).name.contains("Premium Series")) {
                        i.putExtra(Constants.IS_BRAND_FILTER_DISPLAY, true);
                    }
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
            }
        });
    }


}
