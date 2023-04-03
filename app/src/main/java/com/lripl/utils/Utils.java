package com.lripl.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lripl.dealer.BaseActivity;
import com.lripl.dealer.R;
import com.lripl.entities.Products;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {
    public static List<Products> product_enquiry = new ArrayList<>();

    public static void showAlertDialog(Context context,String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);
        alertDialog.setButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if(!((BaseActivity)context).isFinishing()) {
            alertDialog.show();
        }
    }

    public static String getDateFormatWithMonth(Date millis) {
        SimpleDateFormat actualformat = new SimpleDateFormat("dd MMMM, yyyy hh:mm a", Locale.US);
        //Date actualdate = new Date(millis);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();

            String date_string = actualformat.format(millis);

            return date_string;
        } catch (Exception e) {

        }
        return "";
    }
    public static String NullChecker(String var) {
        if (var == null) {
            return "";
        } else {
            if (var.equals("null")) {
                return "";
            } else {
                return var;
            }
        }
    }

    public static void showKeyboard(Activity activity, EditText forEditText) {
        forEditText.requestFocus();
        InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Activity activity, EditText forEditText) {
        InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(forEditText.getApplicationWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static long getWaitTimeExp(int retryCount) {
        long waitTime = ((long) Math.pow(2, retryCount) * 100L);
        return waitTime;
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
