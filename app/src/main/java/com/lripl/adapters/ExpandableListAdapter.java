package com.lripl.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.model.MenuModel;

import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;


    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Log.i("ExpandableListAdapter", "groupPosition"+groupPosition + " childPosition "+childPosition);
        MenuModel childModel = getChild(groupPosition, childPosition);
        final String childText = childModel.menuName;
        boolean isItemSelected = childModel.isItemSelected();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.filter_list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setTypeface(txtListChild.getTypeface(), isItemSelected ? Typeface.BOLD : Typeface.NORMAL);
        TextView imgItemSelected = convertView.findViewById(R.id.img_filter_item_selected);
         imgItemSelected.setVisibility(isItemSelected ? View.VISIBLE : View.GONE);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.filter_list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView lbFilterValue = convertView.findViewById(R.id.lbFilterValue);
        if(getGroup(groupPosition).hasChildren) {
            getSelectedStateFilterValue(getGroup(groupPosition), lbFilterValue);
        }else{
            lbFilterValue.setText("");
        }

        TextView imgArrow = convertView.findViewById(R.id.img_filter_arrow);
        TextView imgSelected = convertView.findViewById(R.id.img_filter_selected);

        imgArrow.setVisibility(!getGroup(groupPosition).hasChildren ? View.GONE : View.VISIBLE);
        imgSelected.setVisibility(!getGroup(groupPosition).hasChildren && getGroup(groupPosition).isItemSelected() ? View.VISIBLE : View.GONE);


        return convertView;
    }

    private void getSelectedStateFilterValue(MenuModel groupMenuModel, TextView filterValue){

        StringBuilder stateBuilder = new StringBuilder();
        StringBuilder brandBuilder = new StringBuilder();

        if(groupMenuModel.menuName.equals("State")){
            for(MenuModel childModel : listDataChild.get(groupMenuModel)){
                if(childModel.isItemSelected()){
                    stateBuilder.append(childModel.menuName+", ");
                }

            }
            if(!TextUtils.isEmpty(stateBuilder.toString())) {
                filterValue.setText(stateBuilder.toString().substring(0, stateBuilder.length()-2));
            }else{
                filterValue.setText("");
            }
        }

        if(groupMenuModel.menuName.equals("Brand")){
            for(MenuModel childModel : listDataChild.get(groupMenuModel)){
                if(childModel.isItemSelected()){
                    brandBuilder.append(childModel.menuName+", ");
                }
            }
            if(!TextUtils.isEmpty(brandBuilder.toString())) {
                filterValue.setText(brandBuilder.toString().substring(0, brandBuilder.length()-2));
            }else{
                filterValue.setText("");
            }

        }

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}