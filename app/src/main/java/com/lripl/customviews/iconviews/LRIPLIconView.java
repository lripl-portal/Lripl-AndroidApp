package com.lripl.customviews.iconviews;

import android.content.Context;
import android.util.AttributeSet;


public class LRIPLIconView extends LRIPLBaseIconView {


    public LRIPLIconView(Context context) {
        super(context);
    }

    public LRIPLIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getIconFontFile() {
        return "fonts/font_icon.ttf";
    }

}