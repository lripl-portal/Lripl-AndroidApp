package com.lripl.customviews.views;


import com.lripl.utils.FontManager;

/**
 * A {@link LRIPLFontStyle} Enum for font style that contains all the font id
 *
 * <p>This will be used when you use any of custom view {@link LRIPLTextView} {@link LRIPLEditText} {@link LRIPLButton} in your layouts
 * once font id is being passed then corresponding font properties will be given by {@link LRIPLFontStyle}
 * You should only need to manually use this class when writing custom views.</p>
 *
 */
public enum LRIPLFontStyle {

    BOLD_T1("T1", FontManager.NOTO_SANS_BOLD, 18, false),
    SEMI_BOLD_T2("T2", FontManager.NOTO_SANS_SEMI_BOLD, 16, false),
    REGULAR_T3("T3", FontManager.NOTO_SANS_REGULAR, 16, false),
    REGULAR_T4("T4", FontManager.NOTO_SANS_REGULAR, 14, false),
    MEDIUM_T5("T5", FontManager.NOTO_SANS_MEDIUM, 14, false),
    SEMI_BOLD_T6("T6", FontManager.NOTO_SANS_SEMI_BOLD, 14, false),
    BOLD_T7("T7", FontManager.NOTO_SANS_BOLD, 14, false),
    REGULAR_T8("T8", FontManager.NOTO_SANS_REGULAR, 12, false),
    MEDIUM_T9("T9", FontManager.NOTO_SANS_MEDIUM, 12, false),
    MEDIUM_CAPS_T10("T10", FontManager.NOTO_SANS_MEDIUM, 12, true),
    SEMI_BOLD_CAPS_T11("T11", FontManager.NOTO_SANS_SEMI_BOLD, 12, true),
    SEMI_BOLD_T12("T12", FontManager.NOTO_SANS_SEMI_BOLD, 12, false),
    BOLD_T13("T13", FontManager.NOTO_SANS_BOLD, 12, false),
    REGULAR_T14("T14", FontManager.NOTO_SANS_REGULAR, 10, false),
    SEMI_BOLD_T15("T15", FontManager.NOTO_SANS_SEMI_BOLD, 10, false),
    SEMI_BOLD_T16("T16", FontManager.NOTO_SANS_SEMI_BOLD, 8, false),
    SEMI_BOLD_T17("T17", FontManager.NOTO_SANS_SEMI_BOLD, 18, false),
    REGULAR_T18("T18", FontManager.NOTO_SANS_REGULAR, 18, false),
    BOLD_T19("T19", FontManager.NOTO_SANS_BOLD, 22, false),
    REGULAR_T20("T20", FontManager.NOTO_SANS_REGULAR, 11, false),
    MEDIUM_T21("T21", FontManager.NOTO_SANS_MEDIUM, 18, false),
    SEMI_BOLD_T22("T22", FontManager.NOTO_SANS_SEMI_BOLD, 10, false),
    BOLD_T23("T23", FontManager.NOTO_SANS_BOLD, 16, false),
    MEDIUM_T24("T24", FontManager.NOTO_SANS_MEDIUM, 16, false),
    SEMI_BOLD_T25("T25", FontManager.NOTO_SANS_SEMI_BOLD, 13, false),
    REGULAR_T26("T26", FontManager.NOTO_SANS_REGULAR, 13, false),
    SEMI_BOLD_T27("T27", FontManager.NOTO_SANS_SEMI_BOLD, 26, false),
    MEDIUM_T28("T28", FontManager.NOTO_SANS_MEDIUM, 20, false),

    //button style
    SEMI_BOLD_B1("B1", FontManager.NOTO_SANS_SEMI_BOLD, 14, false),
    SEMI_BOLD_B2("B2", FontManager.NOTO_SANS_SEMI_BOLD, 14, true),
    MEDIUM_B3("B3", FontManager.NOTO_SANS_MEDIUM, 12, false),

    //edit text style
    MEDIUM_I1("I1", FontManager.NOTO_SANS_MEDIUM, 16, false),
    REGULAR_I2("I2", FontManager.NOTO_SANS_REGULAR, 14, false),
    REGULAR_I3("I3", FontManager.NOTO_SANS_REGULAR, 16, false),
    REGULAR_I4("I4", FontManager.NOTO_SANS_REGULAR, 11, false),
    MEDIUM_I5("I5", FontManager.NOTO_SANS_MEDIUM, 14, false);


    private final String id;
    private final String fontName;
    private final int textSize;
    private final boolean isCaps;

    LRIPLFontStyle(String id, String fontName, int textSize, boolean isCaps) {
        this.id = id;
        this.fontName = fontName;
        this.textSize = textSize;
        this.isCaps = isCaps;
    }

    /** This method return enum based on font id that is being passed
     * @return Zee5FontStyle font style enum
     */
    public static LRIPLFontStyle getORFontStyle(String id) {
        for (LRIPLFontStyle zee5FontStyle : LRIPLFontStyle.values()) {
            if (zee5FontStyle.id.equalsIgnoreCase(id)) return zee5FontStyle;
        }
        return REGULAR_T4;
    }

    /** return font id
     * @return font id
     */
    public String  getId() {
        return id;
    }

    /** Get font name
     * @return font name
     */
    public String getFontName() {
        return fontName;
    }

    /** Get font size
     * @return font size
     */
    public int getFontSize(){
        return textSize;
    }

    /** Get text in caps flag
     * @return true when text should be displayed in the CAPS
     */
    public boolean isTextInCaps(){
        return isCaps;
    }
}
