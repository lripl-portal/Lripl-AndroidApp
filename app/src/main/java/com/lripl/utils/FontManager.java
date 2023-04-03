package com.lripl.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.lripl.customviews.views.LRIPLButton;
import com.lripl.customviews.views.LRIPLEditText;
import com.lripl.customviews.views.LRIPLTextView;


/**
 * A {@link FontManager} font manager class that has reference of all the font file which are stored in asset folder
 *
 * <p>This will automatically be used when you use custom view {@link LRIPLTextView} {@link LRIPLEditText} {@link LRIPLButton}
 * and add this line app:zee5FontStyle="T12" here T12 is the font id
 * return {@link Typeface} based on font name
 */

public class FontManager {

    private static final String FONT_ROOT = "fonts/";
    public static final String NOTO_SANS_BOLD = FONT_ROOT + "NotoSans-Bold.ttf";
    public static final String NOTO_SANS_MEDIUM = FONT_ROOT + "NotoSans-Medium.ttf";
    public static final String NOTO_SANS_REGULAR = FONT_ROOT + "NotoSans-Regular.ttf";
    public static final String NOTO_SANS_SEMI_BOLD = FONT_ROOT + "NotoSans-SemiBold.ttf";

    /**
     *
     * @param context context
     * @param font font name
     * @return Typeface object
     */
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}