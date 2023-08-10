package com.lripl.entities;

import androidx.room.Entity;
import androidx.room.TypeConverters;

public class BrandList {

    String brandName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
