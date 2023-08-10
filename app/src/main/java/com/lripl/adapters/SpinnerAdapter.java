package com.lripl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lripl.entities.States;
import com.lripl.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<States> stateList = new ArrayList<>();

    public SpinnerAdapter(Context context){
        this.context = context;
        this.stateList = stateList;

    }

    @Override
    public int getCount() {
        return  stateList.size();
    }

    @Override
    public States getItem(int position) {
        return stateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_selectable_list_item,null);
        TextView txt = convertView.findViewById(android.R.id.text1);
        txt.setText(Utils.NullChecker(getItem(position).name));

        return convertView;
    }

    public void setStateList(List<States> stateList){
        this.stateList = stateList;
        notifyDataSetChanged();
    }
}
