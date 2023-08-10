package com.lripl.dealer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.lripl.utils.Utils;

public class SMSBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"-----SMS Receiver-----------",Toast.LENGTH_LONG).show();
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";
        Log.i(SMSBroadCastReceiver.class.getSimpleName(),"-----------SMSBroadCastReceiver-----------"+bundle);
        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_str = smsm[i].getMessageBody().toString();
                //Check here sender is yours
                Intent smsIntent = new Intent("otp");
                smsIntent.putExtra("message",getOtpFromMessageBody(sms_str));
                LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
            }
        }
    }

    public String getOtpFromMessageBody(String messagebody){
        try {

            messagebody = messagebody.split("\\.")[0];
            String otp = messagebody.replaceAll("[^0-9]", "");
            Log.i(SMSBroadCastReceiver.class.getSimpleName(),"-----------OTP-----------"+otp);
            return otp;
        }catch (Exception e){

        }
        return Utils.NullChecker(null);
    }

}
