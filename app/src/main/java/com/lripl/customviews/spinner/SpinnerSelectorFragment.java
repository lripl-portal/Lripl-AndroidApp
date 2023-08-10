package com.lripl.customviews.spinner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lripl.dealer.R;
import com.lripl.entities.States;

import java.util.List;

public class SpinnerSelectorFragment extends BottomSheetDialogFragment {
    private RecyclerView mSelectorRecycler;
    private TextView mSelectorTitle;
    private TextView mSelectorSelection;
    private SelectorAdapter mSelectorAdapter;
    private List<States> selectorArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity activity;
    private View view;
    private String mTitle;
    private int selectedValue = 0;
    private SelectorItemClickListener selectorItemClickListener;
    private ConstraintLayout selector_view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;


    }


    public void setSelectorItemClickListener(SelectorItemClickListener selectorItemClickListener) {
        this.selectorItemClickListener = selectorItemClickListener;
    }


    /*
     * Caller should implement TitleBar Visiblity at their respective screens.
     * Before calling selector screen caller should set visibility of titlebar to gone.
     * And after select any value from selector caller should set titlebar visibility to Visible or according to requierment.
     *
     * */

    public void setDialogData (List<States> selectorList, String screenTitle) {
        this.selectorArrayList = selectorList;
        this.mTitle = screenTitle;

    }

    public SpinnerSelectorFragment setSelectedValue(int selectedValue) {
        this.selectedValue = selectedValue;
        return this;
    }


    public void notifyAdapter() {
        mSelectorAdapter.notifyDataSetChanged();
        mSelectorTitle.setText(mTitle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        //super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.bottom_sheet_dialog_base_layout, null);
        dialog.setContentView(view);

        RelativeLayout dialogLayout = view.findViewById(R.id.dialog_layout);

        mSelectorRecycler = dialogLayout.findViewById(R.id.selector_container);
        mSelectorTitle = dialogLayout.findViewById(R.id.selector_screen_title);
        mSelectorSelection = dialogLayout.findViewById(R.id.selector_selection);
        mSelectorTitle.setText(mTitle);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mSelectorRecycler.setLayoutManager(mLayoutManager);
        mSelectorAdapter = new SelectorAdapter(selectorItemClickListener, selectorArrayList, selectedValue);
        mSelectorRecycler.setAdapter(mSelectorAdapter);
        mSelectorSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectorItemClickListener.itemClicked(selectedValue);

            }
        });
        if (selectedValue >= 0)
            mSelectorRecycler.scrollToPosition(selectedValue);

        dialogLayout.findViewById(R.id.dialog_divider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorItemClickListener.itemClicked(selectedValue);
            }
        });

        //TODO
    /*
    Right now Selector works two types of object data
    DisplayDTO (from Displaylangauge)
    Strings (used in all other files)
    This needs to be changed to a more GenericModelObject
    */

        /*if (selectorArrayList.get(0) instanceof States) {
            selector_view.setBackgroundColor(0);
            mSelectorSelection.setVisibility(View.GONE);
            mSelectorTitle.setVisibility(View.GONE);
        } else {
            mSelectorSelection.setVisibility(View.VISIBLE);
            mSelectorTitle.setVisibility(View.VISIBLE);
        }*/

    }


}