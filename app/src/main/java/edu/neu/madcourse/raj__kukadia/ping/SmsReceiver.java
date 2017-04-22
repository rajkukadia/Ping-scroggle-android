package edu.neu.madcourse.raj__kukadia.ping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

class SmsReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage msg;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            msg = msgs[0];
        } else {
            Object pdus[] = (Object[]) intent.getExtras().get("pdus");
            msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }

        String number = msg.getOriginatingAddress();
        String message = msg.getMessageBody();
        String code[] = message.split("\n");
        String codeString = "";
        char a[]=code[1].toCharArray();
        for(char temp:a){
            codeString+=temp+" ";
        }
               UserInformationActivity.verificationCodeArea.setText(codeString);
    }

}