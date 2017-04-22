package edu.neu.madcourse.raj__kukadia.ping;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.neu.madcourse.raj__kukadia.R;

public class UserInformationActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 9999 ;
    private SharedPreferences firstUser = null;
    private Button jumpIn;
    private EditText phoneNumberArea;
    public static EditText verificationCodeArea;
    private TextView info;
    private int code;
    DatabaseReference mRootRef;
    String detectedPhoneNumber;
    private String token;
    private SmsReceiver receiver;
    private boolean permission;
    public static String phoneNumber ;
    public static final String SERVER_KEY = "key=AAAAV5p0wJk:APA91bGhB6kA308eCdUD5OyYe_SBD57BQB2dhxVob9vPBuGm2Angf351qYNDFcuoJ9x2IzvJOHgKqQQ71-MFWfoh6y14hDLnuP9RcCxPld_5okjZeWG_SKqB2Q-AGep8l9dfub7UTrtY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        takeUserDecission();
    }

    private void initData(){
        firstUser = getSharedPreferences("checkFirstUser", MODE_PRIVATE);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
        permission = true;
    }

    private void takeUserDecission(){
        if(firstUser.getBoolean("firstuser", true)){
            handleFirstUser();
        }else{
            phoneNumber = firstUser.getString("phonenumber", null);
            startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
            finish();
        }
    }

    protected boolean checkAndRequestPermissions(){
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionReceiveMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionReadMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionReceiveMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                       grabPhoneNumber();
                        permission = true;
                    } else {
                        permission = false;

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            showDialogOK("SMS access permission is required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }

                }
            }
        }
    }

    private void grabPhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        detectedPhoneNumber = tMgr.getLine1Number();
        phoneNumberArea.setText(detectedPhoneNumber.substring(1,11));
        jumpIn.setVisibility(View.VISIBLE);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void  handleFirstUser(){
        setContentView(R.layout.activity_user_information_ping);
        checkAndRequestPermissions();
        jumpIn = (Button) findViewById(R.id.jump_in_button);
        phoneNumberArea = (EditText) findViewById(R.id.phone_number_area);
        verificationCodeArea = (EditText) findViewById(R.id.verification_code_area);
        info = (TextView) findViewById(R.id.enter_phone_number);
        jumpIn.setText("Verify");
        jumpIn.setOnClickListener(this);
        phoneNumberArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(UserInformationActivity.this, String.valueOf(s.length()), Toast.LENGTH_LONG).show();

                if(s.length()>0) {
                    if (s.charAt(s.length() - 1) < 48 || s.charAt(s.length() - 1) > 57) {
                        phoneNumberArea.setText("");
                        Toast.makeText(UserInformationActivity.this, "Invalid Input", Toast.LENGTH_LONG).show();
                    } else {

                        if (s.length() == 10) {
                            jumpIn.setVisibility(View.VISIBLE);
                        }else {
                            jumpIn.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verificationCodeArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(verificationCodeArea.getText().toString().toCharArray().length==10) {
                    String codeString =formatCode(code);

                    if(codeString.equals(verificationCodeArea.getText().toString())) {
                        jumpIn.setText("Jump In");
                        jumpIn.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(UserInformationActivity.this, "Invalid code", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    jumpIn.setText("Verify");
                    jumpIn.setVisibility(View.GONE);
                }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private String formatCode(int code ){
        String codeString="";
        char a[] = String.valueOf(code).toCharArray();
        for(char temp:a){
            codeString+=temp+" ";
        }
    return codeString;
    }

private void verify(){
    verificationCodeArea.setVisibility(View.VISIBLE);
    jumpIn.setVisibility(View.GONE);
    code = getCode();
    receiver = new SmsReceiver();
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    registerReceiver(receiver, filter);

    if (detectedPhoneNumber != null) {
        SmsManager.getDefault().sendTextMessage(detectedPhoneNumber, null, "Your verification code is:\n" + String.valueOf(code), null, null);
    } else {
        SmsManager.getDefault().sendTextMessage(phoneNumberArea.getText().toString(), null, "Verification Message\n" + String.valueOf(code), null, null);
    }
}
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {


        if(jumpIn.getText().toString().equals("Verify")){
            if(permission) {
                verify();
            }  else{
                checkAndRequestPermissions();
                Toast.makeText(this, "No permission granted", Toast.LENGTH_LONG).show();
            }
        }else{
            jumpIn();
            unregisterReceiver(receiver);
        }
    }

    private void jumpIn(){
        phoneNumber = phoneNumberArea.getText().toString();
        firstUser.edit().putBoolean("firstuser", false).commit();
        firstUser.edit().putString("phonenumber", phoneNumber).commit();
        mRootRef.child("Ping").child("All Users").child(phoneNumberArea.getText().toString()).child("token").setValue(token);
        startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
    }

    private int getCode(){
        Random rNo = new Random();
        final int code = rNo.nextInt((99999 - 10000) + 1) + 10000;
        return code;
    }
}
