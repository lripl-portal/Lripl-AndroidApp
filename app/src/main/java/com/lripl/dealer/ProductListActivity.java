package com.lripl.dealer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.lripl.adapters.ExpandableListAdapter;
import com.lripl.adapters.ProductsGridAdapter;
import com.lripl.dealer.databinding.ProductslistLayoutBinding;
import com.lripl.entities.Products;
import com.lripl.entities.States;
import com.lripl.model.MenuModel;
import com.lripl.network.LoadImageTask;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.ProductViewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductListActivity extends BaseActivity implements ProductsGridAdapter.ItemClickListener {

    ProductslistLayoutBinding productslistLayoutBinding;
    ProductViewmodel productViewmodel;
    ProductsGridAdapter productsGridAdapter;
    CommonViewModelFactory factory;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    Set<String> selectedStateList = new HashSet<>();
    Set<String> selectedBrandList = new HashSet<>();

    ExpandableListAdapter expandableListAdapter;

    int lastExpandedGroupPosition = -1;

    private List<States> statesList = new ArrayList<>();
    private List<Products> productList = new ArrayList<>();

    private int defaultQty;

    // private List<Products> product_enquiry = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.productslist_layout);
        factory = new CommonViewModelFactory(this);
        productViewmodel = ViewModelProviders.of(this, factory).get(ProductViewmodel.class);
        productslistLayoutBinding.setProductViewModel(productViewmodel);
        productslistLayoutBinding.setLifecycleOwner(this);

        productslistLayoutBinding.gridLayout.setLayoutManager(new GridLayoutManager(this, 3));
        //productslistLayoutBinding.gridLayout.addItemDecoration(new GridSpacingItemDecoration(3, 25,false));
        productsGridAdapter = new ProductsGridAdapter(this);
        productsGridAdapter.setClickListener(this);
        productslistLayoutBinding.gridLayout.setAdapter(productsGridAdapter);


        productslistLayoutBinding.filterDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.black));

        productslistLayoutBinding.filterDrawerLayout.closeDrawer(GravityCompat.END);

        productslistLayoutBinding.filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // productslistLayoutBinding.gridLayout.setVisibility(View.GONE);
                if (productslistLayoutBinding.filterDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    productslistLayoutBinding.filterDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    populateExpandableList();
                    String searchedText = productslistLayoutBinding.editTextProductSearch.getText().toString();
                    if(!TextUtils.isEmpty(searchedText) && searchedText.length() > 0) {
                        resetSearchValue();
                    }
                    productslistLayoutBinding.filterDrawerLayout.setElevation((float) 2.0);
                    productslistLayoutBinding.filterDrawerLayout.openDrawer(GravityCompat.END);

                }
            }
        });

        productslistLayoutBinding.filterDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                productslistLayoutBinding.filterDrawerLayout.setElevation((float) 0.0);
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


        productslistLayoutBinding.editTextProductSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (productslistLayoutBinding.filterDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                        productslistLayoutBinding.filterDrawerLayout.closeDrawer(GravityCompat.END);
                    }
                }
            }
        });

        productslistLayoutBinding.icSearchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               resetSearchValue();
            }
        });

        productslistLayoutBinding.editTextProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("ProductListActivity", "text: " + charSequence.toString());
                productslistLayoutBinding.icSearchDelete.setVisibility(charSequence.toString().length() > 0 ? View.VISIBLE : View.GONE);
                productViewmodel.getProductListBySearch(charSequence.toString());
                productViewmodel.getSearchedProductListLiveData().observeForever(new Observer<List<Products>>() {
                    @Override
                    public void onChanged(@Nullable List<Products> productsList) {
                        Log.i("ProductListActivity", "searched product size : " + productsList.size());
                        setProductListView(productsList);
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        productViewmodel.getStatesLiveData().observe(this, new Observer<List<States>>() {
            @Override
            public void onChanged(@Nullable List<States> states) {
                Log.i("ProductListActivity", "State Size" + states.size());
                statesList = states;
                prepareFilterMenuData();
            }
        });

        productViewmodel.getProductListLiveData().observe(this, new Observer<List<Products>>() {
            @Override
            public void onChanged(@Nullable List<Products> productsList) {
                productList = productsList;
                setProductListView(productsList);
                prepareFilterMenuData();
            }
        });


        productslistLayoutBinding.btnCancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productslistLayoutBinding.filterDrawerLayout.closeDrawer(GravityCompat.END);
                if (selectedStateList.size() == 0 && selectedBrandList.size() == 0) {
                    headerList.get(0).setItemSelected(true);
                }
            }
        });
        productslistLayoutBinding.btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productslistLayoutBinding.filterDrawerLayout.closeDrawer(GravityCompat.END);
                Log.i("ProductListActivity", "selected state size " + selectedStateList.size());
                Log.i("ProductListActivity", "selected brand size " + selectedBrandList.size());

                Log.i("ProductListActivity", "selected ststae  " + selectedStateList.toString());
                productViewmodel.getFilteredProductList(selectedStateList, selectedBrandList, productList);

                productViewmodel.getFilteredProductListLiveData().observeForever(new Observer<List<Products>>() {
                    @Override
                    public void onChanged(@Nullable List<Products> productsList) {
                        Log.i("ProductListActivity", "filtered product size : " + productsList.size());
                        setProductListView(productsList);
                    }
                });


            }
        });

    }

    private void resetSearchValue(){
        Utils.hideSoftKeyboard(ProductListActivity.this);
        productslistLayoutBinding.editTextProductSearch.setText("");
        productViewmodel.getProductListBySearch("");
        productViewmodel.getSearchedProductListLiveData().observeForever(new Observer<List<Products>>() {
            @Override
            public void onChanged(@Nullable List<Products> productsList) {
                Log.i("ProductListActivity", "searched product size : " + productsList.size());
                setProductListView(productsList);
            }
        });
    }

    private void setProductListView(List<Products> productsList) {
        productsGridAdapter.setData(productsList);

        productslistLayoutBinding.productNotFoundLayout.setVisibility(productsList != null && productsList.size() == 0 ? View.VISIBLE : View.GONE);
        if( productslistLayoutBinding.editTextProductSearch.getText().length() > 0) {
            productslistLayoutBinding.txtNoProductFound.setText(productsList != null && productsList.size() == 0 ? "No products found for \n" + "\"" + productslistLayoutBinding.editTextProductSearch.getText().toString().toUpperCase() + "\"" : "");
        }else{
            productslistLayoutBinding.productFilterLayout.setVisibility(productsList != null && productsList.size() == 0 ? View.GONE : View.VISIBLE);
            productslistLayoutBinding.txtNoProductFound.setText(productsList != null && productsList.size() == 0 ? "No products found for \n" + "\"" + productViewmodel.itemName + "\"" : "");
            productslistLayoutBinding.filterDivider.setVisibility(productsList != null && productsList.size() == 0 ? View.GONE : View.VISIBLE);
            productslistLayoutBinding.productSearchLayout.setVisibility(productsList != null && productsList.size() == 0 ? View.GONE : View.VISIBLE);
        }
        productslistLayoutBinding.txtProductCount.setText(productsList.size() + " Products");
    }


    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        productslistLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(getIntent().getStringExtra(Constants.PARSE_STRING_FEILD));
        iconBack.setVisibility(View.VISIBLE);
        iconMenu.setVisibility(View.INVISIBLE);
        iconNotification.setVisibility(View.INVISIBLE);
        iconCart.setVisibility(View.VISIBLE);
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductListActivity.this, CartListActivity.class);
                startActivity(i);
            }
        });
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
                Intent i = new Intent(ProductListActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_contactus:
                Intent help = new Intent(ProductListActivity.this, HelpActivity.class);
                startActivity(help);
                break;
            case R.id.nav_about_us:
                Intent aboutUs = new Intent(ProductListActivity.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent i = new Intent(ProductListActivity.this,ProductDetailActivity.class);
        i.putExtra(Constants.PARSE_PRODUCT, productsGridAdapter.getItem(position));
        startActivity(i);
        //prepareDialog(productsGridAdapter.getItem(position));
    }

    private void showAlertDialog(Context context, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);
        alertDialog.setButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ProductListActivity.this.finish();
            }
        });

        if (context != null) {
            alertDialog.show();
        }
    }

    /*private void prepareDialog(final Products products) {
        *//*final ProductDetailsDialog productDetailsDialog = new ProductDetailsDialog();
        productDetailsDialog.setDialogData(products);
        productDetailsDialog.show(getSupportFragmentManager(), "Product Details");*//*

        final Dialog dialog = new Dialog(this, R.style.BaseActivityTheme);
        defaultQty = 1;
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.product_details_layout, null);
        dialog.setContentView(view);
        final ProductDetailsDialogLayoutBinding productDetailsLayoutBinding = DataBindingUtil.bind(view);

        productDetailsLayoutBinding.txtSelectedProdName.setText(products.name);
        productDetailsLayoutBinding.selectedProdImg.setMaxZoom(5);

        productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));
        products.quantity = defaultQty;

        productDetailsLayoutBinding.closeProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final String url = RestApiClient.IMAGES_PATH + products.imageurl;
        new LoadImageTask(productDetailsLayoutBinding.progressWheel, productDetailsLayoutBinding.selectedProdImg).execute(url);

        productDetailsLayoutBinding.txtProdAddQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultQty++;
                products.quantity = defaultQty;
                productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));
            }
        });

        productDetailsLayoutBinding.txtProdRemoveQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultQty != 1) {
                    defaultQty--;
                    products.quantity = defaultQty;
                    productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));

                }
            }
        });

        productDetailsLayoutBinding.btnAddForEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!isProductAdded(products.product_id)) {
                    Utils.product_enquiry.add(products);
                    enquiry_livedata.setValue(Utils.product_enquiry);
                } else {
                    Utils.showAlertDialog(ProductListActivity.this, "This product is already added in you cart list.");
                }
            }
        });
        dialog.show();
    }*/


    private boolean isProductAdded(String product_id) {
        boolean isadded = false;
        for (Products products : Utils.product_enquiry) {
            if (product_id.equalsIgnoreCase(products.product_id)) {
                isadded = true;
                break;
            }
        }
        return isadded;
    }

    private void prepareFilterMenuData() {
        if (productList != null && productList.size() > 0 && statesList != null && statesList.size() > 0) {
            MenuModel menuModel = new MenuModel("Pan India", "All", true, false); //Menu of Android Tutorial. No sub menus
            menuModel.setItemSelected(true);
            headerList.add(menuModel);

            if (!menuModel.hasChildren) {
                childList.put(menuModel, null);
            }
            menuModel = new MenuModel("State", "State", true, true); //Menu of Java Tutorials
            headerList.add(menuModel);

            List<MenuModel> childModelsList = new ArrayList<>();
            final Set<States> uniqueStateList = new HashSet<>();

            for (Products product : productList) {
                if(!TextUtils.isEmpty(product.state_id)) {
                    String[] splitStateList = product.state_id.split(",");
                    for(String stateId : splitStateList){
                        for (States state : statesList) {
                            if (state.state_id.equals(stateId)) {
                                uniqueStateList.add(state);
                                break;
                            }
                        }
                    }

                }
            }

            for (States state : uniqueStateList) {
                MenuModel childModel = new MenuModel(state.name, state.state_id, false, false);
                childModelsList.add(childModel);
            }
            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }

            if (!getIntent().getBooleanExtra(Constants.IS_BRAND_FILTER_DISPLAY, false)) {

                menuModel = new MenuModel("Brand", "Brand", true, true); //Menu of Python Tutorials
                headerList.add(menuModel);

                Set<String> brandList = new HashSet<>();
                for (Products products : productList) {
                    Log.i("ProductListActivity", "brand"+products.brandName);
                    if(!TextUtils.isEmpty(products.brandName)) {
                        String[] splitedBrandList = products.brandName.split(",");
                        Collections.addAll(brandList, splitedBrandList);
                    }
                }

                childModelsList = new ArrayList<>();

                for (String brand : brandList) {
                    MenuModel childModel = new MenuModel(brand, brand, false, false);
                    childModelsList.add(childModel);

                }
                if (menuModel.hasChildren) {
                    childList.put(menuModel, childModelsList);
                }
            }
        }

    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        productslistLayoutBinding.filterExpandableListView.setAdapter(expandableListAdapter);

        productslistLayoutBinding.filterExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });

        productslistLayoutBinding.filterExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != lastExpandedGroupPosition) {
                    productslistLayoutBinding.filterExpandableListView.collapseGroup(lastExpandedGroupPosition);
                    lastExpandedGroupPosition = groupPosition;
                }
                if (!headerList.get(groupPosition).hasChildren) {
                    headerList.get(groupPosition).setItemSelected(true);
                    selectedStateList.clear();
                    selectedBrandList.clear();

                    for (Map.Entry<MenuModel, List<MenuModel>> entry : childList.entrySet()) {

                        if (entry.getKey().hasChildren) {
                            for (MenuModel childMenuModel : entry.getValue()) {
                                childMenuModel.setItemSelected(false);
                            }
                        }
                        expandableListAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

        productslistLayoutBinding.filterExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    // Toast.makeText(ProductListActivity.this, model.menuName, Toast.LENGTH_SHORT).show();
                    headerList.get(0).setItemSelected(false);
                    headerList.get(groupPosition).setItemSelected(!model.isItemSelected);
                    model.setItemSelected(!model.isItemSelected);
                    if (headerList.get(groupPosition).menuName.equalsIgnoreCase("State")) {
                        if (model.isItemSelected) {
                            selectedStateList.add(model.menuId);
                        } else {
                            selectedStateList.remove(model.menuId);
                        }
                    }

                    if (headerList.get(groupPosition).menuName.equalsIgnoreCase("Brand")) {
                        if (model.isItemSelected) {
                            selectedBrandList.add(model.menuName);
                        } else {
                            selectedBrandList.remove(model.menuName);
                        }
                    }

                    expandableListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

}
