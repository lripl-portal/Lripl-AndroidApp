package com.lripl.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lripl.entities.BrandList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BrandConverter {

    @TypeConverter
    public List<BrandList> toBrandValuesList(String value) {

        if (value == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BrandList>>() {
        }.getType();
        List<BrandList> productBrandList = gson.fromJson(value, type);
        return productBrandList;
    }

    @TypeConverter
    public String fromBrandValuesList(ArrayList<BrandList> list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BrandList>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
