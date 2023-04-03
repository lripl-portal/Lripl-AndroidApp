package com.lripl.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lripl.database.AppDatabase;
import com.lripl.dealer.EnquiryItemDetails;
import com.lripl.dealer.R;
import com.lripl.dealer.databinding.EnquiryItemlistItemBinding;
import com.lripl.entities.OrderItem;
import com.lripl.entities.Products;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EnquiryItemsListAdapter extends RecyclerView.Adapter<EnquiryItemsListAdapter.ItemViewHolder> {

    private Context context;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private ItemClickListener mClickListener;

    public EnquiryItemsListAdapter(Context context,List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.enquiry_itemlist_item, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        OrderItem orderItem = getItem(i);
        itemViewHolder.enquiryItemlistItemBinding.txtProductName.setText(orderItem.product_name);
        itemViewHolder.enquiryItemlistItemBinding.txtProductQty.setText(orderItem.quantity + "");
        itemViewHolder.enquiryItemlistItemBinding.txtProductStatus.setText(orderItem.status);
        /*final LiveData<Products> product = AppDatabase.getInstance(context).productsDao().getProductByProductId(orderItem.product_id);
        product.observe((EnquiryItemDetails) context, new Observer<Products>() {
            @Override
            public void onChanged(@Nullable Products products) {
                String url = RestApiClient.IMAGES_PATH+products.imageurl;
                Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(itemViewHolder.enquiryItemlistItemBinding.imgProduct);
            }
        });*/
        if(!Utils.NullChecker(orderItem.product_image).isEmpty()){
            String url = RestApiClient.IMAGES_PATH + orderItem.product_image;
            Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(itemViewHolder.enquiryItemlistItemBinding.imgProduct);
        }

    }

    public OrderItem getItem(int position) {
        return orderItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EnquiryItemlistItemBinding enquiryItemlistItemBinding;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            enquiryItemlistItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
