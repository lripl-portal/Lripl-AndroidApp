package com.lripl.customviews.spinner;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.entities.States;
import java.util.List;

public class SelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SelectorItemClickListener itemClickListener;
    private List<States> selectorListItem;
    private View preView;
    private int selected;


    public SelectorAdapter(SelectorItemClickListener itemClickListener, List<States> selectorListItem, int selected) {

        this.itemClickListener = itemClickListener;
        this.selectorListItem = selectorListItem;
        this.selected = selected;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new SelectorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_selector_recycler_item, parent, false),itemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                final SelectorViewHolder selectorViewHolder = (SelectorViewHolder) holder;

        if (position == selected) {
            selectorViewHolder.itemView.setSelected(true);
            selectorViewHolder.itemView.findViewById(R.id.selectionImageSelector).setVisibility(View.VISIBLE);
            selectorViewHolder.mSelectorContent.setTextColor(selectorViewHolder.mSelectorContent.getContext().getResources().getColor(R.color.white));
            preView = selectorViewHolder.itemView;
        } else {
            selectorViewHolder.itemView.setSelected(false);
            selectorViewHolder.itemView.findViewById(R.id.selectionImageSelector).setVisibility(View.GONE);
            selectorViewHolder.mSelectorContent.setTextColor(selectorViewHolder.mSelectorContent.getContext().getResources().getColor(R.color.dark_grey));

        }
        selectorViewHolder.mSelectorContent.setText(selectorListItem.get(position).name);

        selectorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemClickListener.itemClicked(selectorViewHolder.getAdapterPosition());
                    }
                }, 100);
                if (preView != null) {
                    preView.setSelected(false);
                    preView.findViewById(R.id.selectionImageSelector).setVisibility(View.GONE);
                    ((TextView) preView.findViewById(R.id.selector_content)).setTextColor(preView.getContext().getResources().getColor(R.color.dark_grey));
                }
                selectorViewHolder.itemView.setSelected(true);
                selectorViewHolder.itemView.findViewById(R.id.selectionImageSelector).setVisibility(View.VISIBLE);
                ((TextView) selectorViewHolder.itemView.findViewById(R.id.selector_content)).setTextColor(selectorViewHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                preView = selectorViewHolder.itemView;

            }
        });


    }

    @Override
    public int getItemCount() {
        return selectorListItem.size();
    }

    public class SelectorViewHolder extends RecyclerView.ViewHolder {
        public TextView mSelectorContent;
        public ConstraintLayout mRecyclerDataContainer;
        public ImageView mSelectionImage;
        public SelectorItemClickListener itemClickListener;
        public View preView;
        public int selected;


        public SelectorViewHolder(final View itemView, final SelectorItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            mSelectorContent = itemView.findViewById(R.id.selector_content);
            mRecyclerDataContainer = itemView.findViewById(R.id.selector_data_container);

        }


    }
}
