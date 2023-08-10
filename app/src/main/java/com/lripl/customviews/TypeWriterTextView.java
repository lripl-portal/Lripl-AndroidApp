package com.lripl.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.lripl.customviews.views.LRIPLFontStyle;
import com.lripl.dealer.R;
import com.lripl.utils.FontManager;

public class TypeWriterTextView extends AppCompatTextView {

    private CharSequence sequence;
    private int mIndex;
    private long delay = 150; //default is 150 milliseconds
    private String fontType;

    public interface TextTypeWriteComplete{
        void onTypeComplete();
    }

    TextTypeWriteComplete textTypeWriteCompleteListener;

    public TypeWriterTextView(Context context) {
        super(context);
    }

    public TypeWriterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public void setTypeWriteListener(TextTypeWriteComplete listener){
        this.textTypeWriteCompleteListener = listener;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            setText(sequence.subSequence(0, mIndex++));
            if (mIndex <= sequence.length()) {
                handler.postDelayed(runnable, delay);
            }else{
              textTypeWriteCompleteListener.onTypeComplete();
            }
        }
    };

    /**
     * Display text with type writer animation
     * @param txt content will be displayed
     */
    public void displayTextWithAnimation(CharSequence txt) {
        sequence = txt;
        mIndex = 0;

        setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }

    /**
     * Change the delay value with this method
     * @param m
     */
    public void setCharacterDelay(long m) {
        delay = m;
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
        boolean isInCaps;
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LRIPLView);
            fontType = array.getString(R.styleable.LRIPLView_lriplTextViewStyle);
            array.recycle();
        }

        fontName = LRIPLFontStyle.getORFontStyle(fontType).getFontName();
        fontTextSize = LRIPLFontStyle.getORFontStyle(fontType).getFontSize();
        isInCaps = LRIPLFontStyle.getORFontStyle(fontType).isTextInCaps();

        setTypeface(FontManager.getTypeface(context, fontName));
        setTextSize(fontTextSize);
        setAllCaps(isInCaps);
    }
}