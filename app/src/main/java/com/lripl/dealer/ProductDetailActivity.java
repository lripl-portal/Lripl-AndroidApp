package com.lripl.dealer;

import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lripl.customviews.FlowLayout;
import com.lripl.customviews.views.LRIPLTextView;
import com.lripl.dealer.databinding.ProductDetailsLayoutBinding;
import com.lripl.dealer.databinding.ProductImageZoomLayoutBinding;
import com.lripl.entities.Products;
import com.lripl.network.LoadImageTask;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.ProductDetailViewModel;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends BaseActivity {

    ProductDetailViewModel productDetailViewModel;
    CommonViewModelFactory factory;
    private Products products;
    private int defaultQty = 100;
    ProductDetailsLayoutBinding productDetailsLayoutBinding;
    LRIPLTextView[] txtBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("ProductDetailActivity", "onCreate");
        products = (Products) getIntent().getSerializableExtra(Constants.PARSE_PRODUCT);
        Log.i("ProductDetailActivity", "onCreate"+products.name);
        setContentLayout(R.layout.product_details_layout);

        productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));
        productDetailsLayoutBinding.txtSelectedProductQty.setSelection(String.valueOf(defaultQty).length());
        productDetailsLayoutBinding.txtSelectedProductQty.setCursorVisible(false);
        productDetailsLayoutBinding.txtProductDescription.setText(TextUtils.isEmpty(products.description) ? getString(R.string.product_description_not_found) : products.description);
        products.quantity = defaultQty;

        String url = RestApiClient.IMAGES_PATH+products.imageurl;
        Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(productDetailsLayoutBinding.selectedProdImg);

        showBrandView();

        productDetailsLayoutBinding.selectedProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               prepareProductImageDialog();
            }
        });

        productDetailsLayoutBinding.txtSelectedProductQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDetailsLayoutBinding.txtSelectedProductQty.setCursorVisible(true);
            }
        });

        productDetailsLayoutBinding.txtSelectedProductQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())) {
                    defaultQty = Integer.parseInt(editable.toString());
                    products.quantity = defaultQty;
                }
            }
        });

        productDetailsLayoutBinding.txtProdAddQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultQty++;
                products.quantity = defaultQty;
                productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));
                productDetailsLayoutBinding.txtSelectedProductQty.setSelection(String.valueOf(defaultQty).length());
            }
        });

        productDetailsLayoutBinding.txtProdRemoveQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultQty != 1) {
                    defaultQty--;
                    products.quantity = defaultQty;
                    productDetailsLayoutBinding.txtSelectedProductQty.setText(String.valueOf(defaultQty));
                    productDetailsLayoutBinding.txtSelectedProductQty.setSelection(String.valueOf(defaultQty).length());

                }
            }
        });

        productDetailsLayoutBinding.btnAddForEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(ProductDetailActivity.this, productDetailsLayoutBinding.txtSelectedProductQty);
                if (!isProductAdded(products.product_id)) {
                    Utils.product_enquiry.add(products);
                    enquiry_livedata.setValue(Utils.product_enquiry);
                    Toast.makeText(v.getContext(), "Product added in your cart list", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Utils.showAlertDialog(ProductDetailActivity.this, "This product is already added in your cart list.");
                }
            }
        });


    }

    private void showBrandView(){
        if(!TextUtils.isEmpty(products.brandName)) {
            String[] splitBrandList = products.brandName.split(",");
            txtBrand = new LRIPLTextView[splitBrandList.length];
            productDetailsLayoutBinding.brandLayout.removeAllViews();
            for (int i = 0; i < txtBrand.length; i++) {
                txtBrand[i] = new LRIPLTextView(getBaseContext());
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.horizontal_spacing = 15;
                params.vertical_spacing = 10;
                txtBrand[i].setPadding(20, 10, 20, 10);
                txtBrand[i].setLayoutParams(params);
                txtBrand[i].setTextSize(12);
                txtBrand[i].setTextColor(getResources().getColor(R.color.dark_grey));
                txtBrand[i].setBackgroundResource(R.drawable.brand_background_shape);
                txtBrand[i].setText(splitBrandList[i]);
                productDetailsLayoutBinding.brandLayout.addView(txtBrand[i]);
            }
        }
    }

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

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        productDetailsLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(products.name);
        iconBack.setVisibility(View.VISIBLE);
        iconMenu.setVisibility(View.INVISIBLE);
        iconNotification.setVisibility(View.INVISIBLE);
        iconCart.setVisibility(View.VISIBLE);
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductDetailActivity.this, CartListActivity.class);
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
                Intent i = new Intent(ProductDetailActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    private void prepareProductImageDialog() {

        final Dialog dialog = new Dialog(this, R.style.BaseActivityTheme);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.product_image_zoom_layout, null);
        dialog.setContentView(view);
        final ProductImageZoomLayoutBinding productImageZoomLayoutBinding = DataBindingUtil.bind(view);
        productImageZoomLayoutBinding.imgImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        String url = RestApiClient.IMAGES_PATH+products.imageurl;
        //Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(productImageZoomLayoutBinding.imgProductZoomImage);
        new LoadImageTask(productImageZoomLayoutBinding.progressWheel, productImageZoomLayoutBinding.imgProductZoomImage).execute(url);

        dialog.show();
    }

}
