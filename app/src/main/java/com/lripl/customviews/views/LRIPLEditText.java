package com.lripl.customviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.lripl.dealer.R;
import com.lripl.utils.FontManager;


/**
 * A {@link LRIPLEditText} custom edit text view which supports font style, text size and text in cap capabilities,
 *
 * <p>This will automatically be used when you use {@link LRIPLEditText} in your layouts
 * and add this line app:zee5EditTextStyle="I5" here I5 is the font id
 * once font id is being passed then corresponding font properties will be automatically applied on the button
 * You should only need to manually use this class when writing custom views.</p>
 */

public class LRIPLEditText extends AppCompatEditText {

    private String fontType;

    public LRIPLEditText(Context context) {
        this(context, null);
    }

    public LRIPLEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public LRIPLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    public void setCustomFontType(String fontType){
        this.fontType = fontType;
        setCustomFont(getContext(), null);
    }

    /**
     * Sets custom font to button
     *
     * @param context context
     * @param attrs   layout attributes
     */

    @SuppressLint("CustomViewStyleable")
    private void setCustomFont(Context context, AttributeSet attrs) {
        String fontName;
        int fontTextSize;
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LRIPLView);
            fontType = array.getString(R.styleable.LRIPLView_lriplEditTextStyle);
            array.recycle();
        }

        fontName = LRIPLFontStyle.getORFontStyle(fontType).getFontName();
        fontTextSize = LRIPLFontStyle.getORFontStyle(fontType).getFontSize();

        setTypeface(FontManager.getTypeface(context, fontName));
        setTextSize(fontTextSize);
    }
}
