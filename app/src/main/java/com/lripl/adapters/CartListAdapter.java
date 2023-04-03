package com.lripl.adapters;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lripl.database.AppDatabase;
import com.lripl.dealer.BaseActivity;
import com.lripl.dealer.CartListActivity;
import com.lripl.dealer.R;
import com.lripl.dealer.databinding.CartListItemLayoutBinding;
import com.lripl.entities.ItemType;
import com.lripl.entities.Products;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private CartItemDeleteListener mClickListener;
    private List<Products> productsList;
    private Context context;


    public CartListAdapter(Context context, List<Products> productsList) {
        this.productsList = productsList;
        this.context = context;
    }

    public Products getProduct(int position) {
        return productsList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_list_item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Products products = getProduct(i);

        Picasso.get().load(RestApiClient.IMAGES_PATH + products.imageurl).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(viewHolder.cartListItemLayoutBinding.imgProductImage);

        viewHolder.cartListItemLayoutBinding.editProductQty.setText(String.valueOf(products.quantity));
        viewHolder.cartListItemLayoutBinding.editProductQty.setSelection(String.valueOf(products.quantity).length());
        viewHolder.cartListItemLayoutBinding.editProductQty.setCursorVisible(false);

        viewHolder.cartListItemLayoutBinding.txtAddQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsList.get(i).quantity++;
                //notifyItemChanged(i);
                viewHolder.cartListItemLayoutBinding.editProductQty.setText(String.valueOf(products.quantity));
                viewHolder.cartListItemLayoutBinding.editProductQty.setSelection(String.valueOf(products.quantity).length());

            }
        });

        viewHolder.cartListItemLayoutBinding.editProductQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.cartListItemLayoutBinding.editProductQty.setCursorVisible(true);
            }
        });

        viewHolder.cartListItemLayoutBinding.txtRemoveQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productsList.get(i).quantity != 1) {
                    productsList.get(i).quantity --;
                    notifyItemChanged(i);
                    viewHolder.cartListItemLayoutBinding.editProductQty.setText(String.valueOf(products.quantity));
                    viewHolder.cartListItemLayoutBinding.editProductQty.setSelection(String.valueOf(products.quantity).length());
                }
            }
        });

        viewHolder.cartListItemLayoutBinding.editProductQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())) {
                    products.quantity = Integer.parseInt(editable.toString());
                }
            }
        });


        viewHolder.cartListItemLayoutBinding.icCartItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemDeleteClick(v, i);
            }
        });
        AppDatabase.getInstance(context).itemTypeDao().getItemType(productsList.get(i).item_type_id).observe((CartListActivity) context, new Observer<ItemType>() {
            @Override
            public void onChanged(@Nullable ItemType itemType) {
                if(productsList != null && productsList.size() > 0 && itemType != null)
                viewHolder.cartListItemLayoutBinding.txtProductName.setText(productsList.get(i).name);
                viewHolder.cartListItemLayoutBinding.txtProductCategoryName.setText(itemType.name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CartListItemLayoutBinding cartListItemLayoutBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartListItemLayoutBinding = DataBindingUtil.bind(itemView);
        }
    }

    // allows clicks events to be caught
    public void setClickListener(CartItemDeleteListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface CartItemDeleteListener {
        void onItemDeleteClick(View view, int position);
    }

    public void setData(List<Products> newData) {
        this.productsList = newData;
        notifyDataSetChanged();
    }


}
