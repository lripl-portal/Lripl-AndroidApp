package com.lripl.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.entities.ItemType;
import com.lripl.network.RestApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridItemsAdapter extends RecyclerView.Adapter<GridItemsAdapter.ViewHolder> {

    private Context context;
    private List<ItemType> itemListLiveData;
    private ItemClickListener mClickListener;

    public GridItemsAdapter(Context context, List<ItemType> itemListLiveData){
        this.context = context;
        this.itemListLiveData = itemListLiveData;
    }

    public ItemType getItemType(int position){
        return itemListLiveData.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_item_layout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if(getItemType(i) != null) {
            String url = RestApiClient.IMAGES_PATH+getItemType(i).imageurl;
            Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(viewHolder.imageView);
            viewHolder.txtView.setText(getItemType(i).name);
        }
        if(((i+1) % 3) >=1){
            viewHolder.frame_left.setVisibility(View.VISIBLE);
        }else{
            viewHolder.frame_left.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemListLiveData == null?0:itemListLiveData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtView;
        FrameLayout frame_left;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item_type);
            txtView = itemView.findViewById(R.id.txt_item_type_name);
            frame_left = itemView.findViewById(R.id.frame_left_line);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setData(List<ItemType> newData) {
        this.itemListLiveData = newData;
        notifyDataSetChanged();
    }
}


