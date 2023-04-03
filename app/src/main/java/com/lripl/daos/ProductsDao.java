package com.lripl.daos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lripl.entities.Items;
import com.lripl.entities.Products;

import java.util.List;
import java.util.Set;

@Dao
public interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProducts(List<Products> productsList);

    @Query("select count(*) from products where item_type_id=:item_type_id")
    LiveData<Integer> getProductsCountByItemTypeId(String item_type_id);

    @Query("select count(*) from products where item_id=:item_id")
    LiveData<Integer> getProductsCountByItemId(String item_id);

    @Query("select * from products where item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Products>> getProductListByItemTypeId(String item_type_id);

    @Query("select * from products where item_id=:item_id ORDER BY name ASC")
    LiveData<List<Products>> getProductListByItemId(String item_id);

    @Query("select * from products where product_id=:product_id LIMIT 1")
    LiveData<Products> getProductByProductId(String product_id);

    @Query("select * from products where (name LIKE '%' || :searchText || '%'  OR company LIKE :searchText || '%') AND item_id=:item_id ORDER BY name ASC")
    LiveData<List<Products>> findProductListByItemId(String searchText, String item_id);

    @Query("select * from products where (name LIKE '%' || :searchText || '%' OR company LIKE :searchText || '%') AND item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Products>> findProductListByItemTypeId(String searchText, String item_type_id);

    @Query("select * from products where company IN (:filteredBrandList) AND state_id IN (:filteredStateList) AND item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Products>> getFilteredProductListByItemTypeId(Set<String> filteredStateList, Set<String> filteredBrandList, String item_type_id);

    @Query("select * from products where state_id IN (:filteredStateList) AND item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Products>> getStateFilteredProductListByItemTypeId(Set<String> filteredStateList, String item_type_id);

    @Query("select * from products where company IN (:filteredBrandList) AND item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Products>> getBrandFilteredProductListByItemTypeId(Set<String> filteredBrandList, String item_type_id);

    @Query("select * from products where brandName IN (:filteredBrandList) AND state_id IN (:filteredStateList) AND item_id=:item_id ORDER BY name ASC")
    LiveData<List<Products>> getFilteredProductListByItemId(Set<String> filteredStateList, Set<String> filteredBrandList, String item_id);

    @Query("select * from products where state_id IN (:filteredStateList) AND item_id=:item_id ORDER BY name ASC")
    LiveData<List<Products>> getStateFilteredProductListByItemId(Set<String> filteredStateList, String item_id);

    @Query("select * from products where company IN (:filteredBrandList) AND item_id=:item_id ORDER BY name ASC")
    LiveData<List<Products>> getBrandFilteredProductListByItemId(Set<String> filteredBrandList, String item_id);
}
