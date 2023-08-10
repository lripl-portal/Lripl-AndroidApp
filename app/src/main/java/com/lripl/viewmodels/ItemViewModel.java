package com.lripl.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.appcompat.app.AppCompatActivity;

import com.lripl.database.AppDatabase;
import com.lripl.entities.Items;
import com.lripl.dealer.ItemsListActivity;
import com.lripl.utils.Constants;

import java.util.List;

public class ItemViewModel extends BaseViewModel {

    ItemsListActivity activity;
    private LiveData<List<Items>> itemsList =  new MutableLiveData<>();
    private String item_type_id;
    private AppDatabase appDatabase;
    ItemViewModel (AppCompatActivity activity){
        this.activity = (ItemsListActivity) activity;
        item_type_id = activity.getIntent().getStringExtra(Constants.PARSE_ITEM_TYPE_ID);
        appDatabase = AppDatabase.getInstance(activity.getApplicationContext());
        itemsList = appDatabase.itemsDao().getItemsList(item_type_id);

    }



  public LiveData<List<Items>> getItemsListLiveData(){
        return itemsList;
  }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void diplayError(String error) {

    }
}
