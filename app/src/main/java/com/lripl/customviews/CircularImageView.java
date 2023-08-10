package com.lripl.customviews;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;


public class CircularImageView extends AppCompatImageView {
    private Context mContext;

    public CircularImageView(Context context) {
        super(context);
        mContext = context;
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public CircularImageView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null) return;

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(mContext.getResources(), bm);
        dr.setCircular(true);
        setImageDrawable(dr);
    }

    public Bitmap getImageBitmap(){
        if(this.getDrawable() instanceof  RoundedBitmapDrawable) {
            RoundedBitmapDrawable drawable = (RoundedBitmapDrawable) this.getDrawable();
            return  drawable.getBitmap();
        }
        return null;
    }
}

