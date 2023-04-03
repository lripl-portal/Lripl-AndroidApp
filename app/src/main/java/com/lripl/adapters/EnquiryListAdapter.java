package com.lripl.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.dealer.databinding.EnquiryOrderItemLayoutBinding;
import com.lripl.entities.OrderItem;
import com.lripl.entities.Orders;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EnquiryListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Orders> mOrdersList;
    private ExpandableListView mExpandableListView;

    public EnquiryListAdapter(Context context, List<Orders> orderList, ExpandableListView expandableListView) {
        this.context = context;
        this.mOrdersList = orderList;
        this.mExpandableListView = expandableListView;
    }

    @Override
    public OrderItem getChild(int groupPosition, int childPosititon) {
        return this.mOrdersList.get(groupPosition).orderItemObjs.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Log.i("EnquiryListAdapter", "groupPosition"+groupPosition + " childPosition "+childPosition);

        OrderItem orderItem = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.enquiry_order_item_layout, null);
        }


        EnquiryOrderItemLayoutBinding enquiryOrderItemLayoutBinding = DataBindingUtil.bind(convertView);

        enquiryOrderItemLayoutBinding.txtProductName.setText(orderItem.product_name);
        enquiryOrderItemLayoutBinding.txtProductQty.setText("No of products: "+orderItem.quantity);
        enquiryOrderItemLayoutBinding.txtProductStatus.setText(orderItem.status);

        if(!Utils.NullChecker(orderItem.product_image).isEmpty()){
            String url = RestApiClient.IMAGES_PATH + orderItem.product_image;
            Picasso.get().load(url).placeholder(R.drawable.dummy_image).error(R.drawable.dummy_image).into(enquiryOrderItemLayoutBinding.imgProduct);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.mOrdersList.get(groupPosition).orderItemObjs == null)
            return 0;
        else
            return this.mOrdersList.get(groupPosition).orderItemObjs.size();

    }

    @Override
    public Orders getGroup(int groupPosition) {
        return this.mOrdersList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mOrdersList.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Log.i("EnquiryListAdapter", "groupPosition"+groupPosition);
        Orders orders = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.enquiry_list_item, null);
        }

        TextView icExpand = convertView.findViewById(R.id.icon_expand);
        TextView txtOrderNumber = convertView.findViewById(R.id.txt_order_number);
        TextView txtOrderCount = convertView.findViewById(R.id.txt_order_products_count);
        TextView txtOrderStatus = convertView.findViewById(R.id.txt_order_status);
        TextView txtOrderDate = convertView.findViewById(R.id.txt_order_created_date);


        icExpand.setText(orders.isGroupExpanded ? "I" : "J");
        txtOrderNumber.setText(orders.order_number);
        txtOrderCount.setText(orders.orderItemObjs.size()+"");
        txtOrderDate.setText( Utils.getDateFormatWithMonth(orders.createdat));
        txtOrderStatus.setText(orders.status);
        txtOrderStatus.setTextColor(setEnquiryOrderStatus(orders.status.toLowerCase()));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    public void setData(List<Orders> newData) {
        this.mOrdersList = newData;
        notifyDataSetChanged();
    }

    private int setEnquiryOrderStatus(String status){
        switch (status) {
            case "in progress":
                return ContextCompat.getColor(context, R.color.status_in_progress);
            case "confirm":
                return ContextCompat.getColor(context, R.color.status_in_confirmed);
            case "closed":
                return ContextCompat.getColor(context, R.color.status_in_closed);
            default:
                return ContextCompat.getColor(context, R.color.status_in_pending);
        }
    }

}
