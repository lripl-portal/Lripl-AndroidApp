package com.lripl.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.lripl.utils.BrandConverter;

import java.util.List;

public class BrandList {

    String brandName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
