package com.lripl.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.entities.Products;
import com.lripl.network.RestApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsGridAdapter extends RecyclerView.Adapter<ProductsGridAdapter.ViewHolder> {
    private Context context;
    private List<Products> productsList = new ArrayList<>();
    private ProductsGridAdapter.ItemClickListener mClickListener;

    public ProductsGridAdapter(Context context){
        this.context = context;
    }
    public Products getItem(int position){
        return productsList.get(position);
    }
    @NonNull
    @Override
    public ProductsGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item_layout,viewGroup,false);
        return new ProductsGridAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(getItem(i) != null) {
            String url = RestApiClient.IMAGES_PATH+getItem(i).imageurl;
            Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(viewHolder.imageView);
            viewHolder.txtView.setText(getItem(i).name);
        }

        /*if(((i+1) % 3) >=1){
            viewHolder.frame_left.setVisibility(View.VISIBLE);
        }else{
            viewHolder.frame_left.setVisibility(View.GONE);
        }*/
    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_product_item);
            txtView = itemView.findViewById(R.id.txt_product_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ProductsGridAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setData(List<Products> newData) {
        this.productsList = newData;
        notifyDataSetChanged();
    }
}
