package com.lripl.utils;

public class Constants {
    public static final String PARSE_PHONE_NUMBER = "parse_phone_number";
    public static final int MAX_RETRIES = 5;
    public static final int HTTP_OK = 200;
    public static final String PARSE_USER_OBJ = "parse_user_obj";
    public static final String PARSE_STRING_FEILD = "parse_string_feild";
    public static final String PARSE_ITEM_TYPE_ID = "parse_item_type_id";
    public static final String PARSE_ITEM_ID = "parse_item_id";
    public static final String PARSE_PRODUCT = "parse_product";
    public static final String PARSE_ORDER_ID = "parse_order_id";
    public static final int  LOGIN_RESULT= 100;
    public static  final String ORDER_STATUS = "PENDING";
    public static final String PLATFORM = "ANDROID";
    public static final String IS_BRAND_FILTER_DISPLAY = "brand_filter";
    public enum PREF_KEYS{
        DEVICE_TOKEN
    }

    /*-----------Selector keys------------*/
    public static final String SELECTOR_DATA = "selector_data ";
    public static final String SCREEN_TITLE = "screen_title ";

    /*------------ Email pattern------------*/
    public static final String EMAIL_PATTERN = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]{2,63}$";

    public static final String PRODUCT_HOME_SCREEN = "product_home_screen";

    public static final String USER_AUTH_TOKEN = "user_auth_token";

    public static final String PROFILE_ERROR_MSG = "Oops, something went wrong, please try again later.";
}
