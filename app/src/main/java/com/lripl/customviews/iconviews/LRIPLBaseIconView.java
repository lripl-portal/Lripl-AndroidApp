package com.lripl.customviews.iconviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public abstract class LRIPLBaseIconView extends AppCompatTextView {

    public abstract String getIconFontFile();

    public LRIPLBaseIconView(Context context) {
        super(context);
        createView();
    }

    public LRIPLBaseIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createView();
    }

    private void createView(){
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),getIconFontFile());
        setTypeface(typeface);

    }
}
