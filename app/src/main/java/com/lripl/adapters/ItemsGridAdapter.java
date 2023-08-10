package com.lripl.adapters;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lripl.database.AppDatabase;
import com.lripl.dealer.R;
import com.lripl.entities.Items;
import com.lripl.network.RestApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemsGridAdapter extends RecyclerView.Adapter<ItemsGridAdapter.ViewHolder> {
    private Context context;
    private List<Items> itemListLiveData = new ArrayList<>();
    private ItemsGridAdapter.ItemClickListener mClickListener;

    public ItemsGridAdapter(Context context){
        this.context = context;

    }

    public Items getItem(int position){
        return itemListLiveData.get(position);
    }
    @NonNull
    @Override
    public ItemsGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.sub_category_layout,viewGroup,false);
        return new ItemsGridAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(getItem(i) != null) {
            String url = RestApiClient.IMAGES_PATH+getItem(i).imageurl;
            Log.i("SubCategory","url>> "+url);
            Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(viewHolder.imageView);
            viewHolder.txtView.setText(getItem(i).name);
            Log.i("SubCategory","item_id>> "+getItem(i).item_id);
            LiveData<Integer> productCountObserver = AppDatabase.getInstance(context).itemsDao().getProductCount(getItem(i).item_id);
            productCountObserver.observe((LifecycleOwner) context, new Observer<Integer>() {
                @Override
                public void onChanged(final Integer productCount) {
                    if(productCount > 1){
                        viewHolder.txtProductCount.setText(context.getString(R.string.txt_products_count, String.valueOf(productCount)));
                    }else{
                        viewHolder.txtProductCount.setText(context.getString(R.string.txt_product_count, String.valueOf(productCount)));
                    }
                }
            });
        }

        /*if(((i+1) % 3) >=1){
            viewHolder.frame_left.setVisibility(View.VISIBLE);
        }else{
            viewHolder.frame_left.setVisibility(View.GONE);
        }*/

    }


    @Override
    public int getItemCount() {
        return itemListLiveData == null?0:itemListLiveData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtView;
        TextView txtProductCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item_type);
            txtView = itemView.findViewById(R.id.txt_item_type_name);
            txtProductCount = itemView.findViewById(R.id.txt_product_count);
            //frame_left = itemView.findViewById(R.id.frame_left_line);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemsGridAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setData(List<Items> newData) {
        this.itemListLiveData = newData;
        notifyDataSetChanged();
    }
}
