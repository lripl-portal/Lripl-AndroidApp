package com.lripl.dealer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lripl.dealer.databinding.OtpLayoutBinding;
import com.lripl.dealer.interfaces.OTPEditTextsViewModelInterface;
import com.lripl.utils.Constants;
import com.lripl.utils.Utils;
import com.lripl.viewmodels.CommonViewModelFactory;
import com.lripl.viewmodels.OtpViewModel;

public class OtpVerificatoinActivity extends BaseActivity implements OTPEditTextsViewModelInterface {

    public OtpLayoutBinding otpLayoutBinding;
    OtpViewModel otpViewModel;
    CommonViewModelFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.otp_layout);
        //otpLayoutBinding = DataBindingUtil.setContentView(this,R.layout.otp_layout);
        factory = new CommonViewModelFactory(this);
        otpViewModel = ViewModelProviders.of(this, factory).get(OtpViewModel.class);
        otpLayoutBinding.setOtpViewModel(otpViewModel);
        otpLayoutBinding.setLifecycleOwner(this);
        otpViewModel.setPinCodeEditText(new EditText[]{otpLayoutBinding.verifyCode1, otpLayoutBinding.verifyCode2, otpLayoutBinding.verifyCode3,
                otpLayoutBinding.verifyCode4}, this);
        otpViewModel.requestFocusForEditTextAtIndex(0);

    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id, null);
        lnr_main_layout.addView(view);
        //toolbar.setVisibility(View.GONE);
        otpLayoutBinding = DataBindingUtil.bind(view);
        txtTitle.setText(getString(R.string.txt_verify_mobile));
        setUpUIForChangeNumber();
        setUpUIForDidNotGetTheOTP();
        cartLayout.setVisibility(View.GONE);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpViewModel.onBackClick(view);
            }
        });

    }

    private void setUpUIForChangeNumber() {

        final String firstPart = getString(R.string.otp_text, "\n" + getIntent().getStringExtra(Constants.PARSE_PHONE_NUMBER));
        String secondPart = "  " + getString(R.string.txt_change_number);
        final String partsCombined = firstPart + secondPart;
        otpLayoutBinding.txtMobileVerifyMsg.setHighlightColor(Color.TRANSPARENT);
        otpLayoutBinding.txtMobileVerifyMsg.setText(partsCombined, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) otpLayoutBinding.txtMobileVerifyMsg.getText();

        //final Spannable otpSentAndChangeNumberTextViewSpannableString = new SpannableString(partsCombined);

        str.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                finish();
            }

            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        }, firstPart.length(), partsCombined.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int stringIndex = partsCombined.indexOf(firstPart);
        str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.semi_dark_grey)), stringIndex, stringIndex + firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), firstPart.length(), partsCombined.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        otpLayoutBinding.txtMobileVerifyMsg.setText(str);
        otpLayoutBinding.txtMobileVerifyMsg.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpUIForDidNotGetTheOTP() {

        otpLayoutBinding.txtResendCode.setHighlightColor(Color.TRANSPARENT);

        final String firstPart = getString(R.string.txt_did_not_get) + " ";
        String secondPart = getString(R.string.txt_resend);
        final String partsCombined = firstPart + secondPart;
        int stringIndex = partsCombined.indexOf(firstPart);

        final SpannableString didntGetTheOTPIdTextViewSpannableString = new SpannableString(partsCombined);

        didntGetTheOTPIdTextViewSpannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                otpViewModel.onResendOtpClick();
            }

            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        }, firstPart.length(), partsCombined.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        didntGetTheOTPIdTextViewSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.semi_dark_grey)), stringIndex, stringIndex + firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        didntGetTheOTPIdTextViewSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), firstPart.length(), partsCombined.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        otpLayoutBinding.txtResendCode.setText(didntGetTheOTPIdTextViewSpannableString);
        otpLayoutBinding.txtResendCode.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public void onResume() {
//        LocalBroadcastManager.getInstance(this).
//                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                if (message.length() == 6 && Utils.isNumeric(message)) {
                    otpViewModel.verifyOtpRequest(otpViewModel.getValues(message));
                }
            }
        }
    };

    private void updateButtonState(boolean isActive) {
        if (isActive) {
            Utils.hideSoftKeyboard(this);
        }
        otpLayoutBinding.btnConfirmOtp.setEnabled(true);
        otpLayoutBinding.btnConfirmOtp.setTextColor(isActive ? getResources().getColor(R.color.white) : getResources().getColor(R.color.semi_dark_grey));
        otpLayoutBinding.btnConfirmOtp.setBackgroundResource(isActive ? R.drawable.verify_btn_bg_selector : R.drawable.negative_btn_bg_selector);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    @Override
    public void onOTPEnteringCompleted(String otpEntered, boolean isActive) {
        updateButtonState(isActive);
    }

}
