package com.lripl.customviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import com.google.android.material.textfield.TextInputLayout;
import android.util.AttributeSet;

import com.lripl.dealer.R;
import com.lripl.utils.FontManager;
/**
 * A {@link LRIPLTextInputLayout} custom text input layout which supports font style family
 *
 * <p>This will automatically be used when you use {@link LRIPLTextInputLayout} in your layouts
 * and add this line app:zee5EditTextStyle="I2" here I2 is the font id
 * once font id is being passed then corresponding font properties will be automatically applied on the button
 * You should only need to manually use this class when writing custom views.</p>
 *
 */

public class LRIPLTextInputLayout extends TextInputLayout {

    private Context context;

    public LRIPLTextInputLayout(Context context) {
        super(context);
        this.context  = context;
        setCustomFont(context, null);
    }

    public LRIPLTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context  = context;
        setCustomFont(context, attrs);
    }

    public LRIPLTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context  = context;
        setCustomFont(context, attrs);
    }

    /**
     * Sets custom font to button
     *
     * @param context context
     * @param attrs layout attributes
     */

    @SuppressLint("CustomViewStyleable")
    private void setCustomFont(Context context, AttributeSet attrs) {
        String fontName, fontType="";

        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LRIPLView);
            fontType = array.getString(R.styleable.LRIPLView_lriplEditTextStyle);
            array.recycle();
        }

        fontName = LRIPLFontStyle.getORFontStyle(fontType).getFontName();
        setTypeface(FontManager.getTypeface(context, fontName));


    }
}
