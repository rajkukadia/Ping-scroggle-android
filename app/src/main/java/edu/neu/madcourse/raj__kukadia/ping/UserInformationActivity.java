package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Random;

import edu.neu.madcourse.raj__kukadia.R;

public class UserInformationActivity extends Activity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1234;
    private SharedPreferences firstUser = null;
    private Button jumpIn;
    private EditText phoneNumberArea;
    private EditText verificationCodeArea;
    private TextView info;
    DatabaseReference mRootRef;
    String detectedPhoneNumber;
    private String token;
    private boolean permission;
    public static String phoneNumber ;
    public static final String SERVER_KEY = "key=AAAAV5p0wJk:APA91bGhB6kA308eCdUD5OyYe_SBD57BQB2dhxVob9vPBuGm2Angf351qYNDFcuoJ9x2IzvJOHgKqQQ71-MFWfoh6y14hDLnuP9RcCxPld_5okjZeWG_SKqB2Q-AGep8l9dfub7UTrtY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstUser = getSharedPreferences("checkFirstUser", MODE_PRIVATE);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();




        if(firstUser.getBoolean("firstuser", true)){
            handleFirstUser();
        }else{
            phoneNumber = firstUser.getString("phonenumber", null);
            startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
            finish();
        }
    }

    protected void checkPermissions() {
        if ((ContextCompat.checkSelfPermission(UserInformationActivity.this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(UserInformationActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission = true;
                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    detectedPhoneNumber = tMgr.getLine1Number();
                    if(detectedPhoneNumber!=null||!detectedPhoneNumber.equals("")){
                        phoneNumberArea.setText(detectedPhoneNumber.substring(1, 11));
                        jumpIn.setVisibility(View.VISIBLE);
                    }
                } else {
                    permission = false;
                }
            }

        }
    }

    private void  handleFirstUser(){
        setContentView(R.layout.activity_user_information_ping);
        checkPermissions();
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



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {


        if(jumpIn.getText().toString().equals("Verify")){
            verificationCodeArea.setVisibility(View.VISIBLE);
            jumpIn.setVisibility(View.GONE);
            int code = getCode();
          //  SmsReceiver receiver = SmsReceiver();

            SmsManager.getDefault().sendTextMessage(detectedPhoneNumber, null,"Verification Message/n"+String.valueOf(code), null, null);
            //send msg
        }else{
            phoneNumber = phoneNumberArea.getText().toString();
            firstUser.edit().putBoolean("firstuser", false).commit();
            firstUser.edit().putString("phonenumber", phoneNumber).commit();
            mRootRef.child("Ping").child("All Users").child(phoneNumberArea.getText().toString()).child("token").setValue(token);
            startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
        }
    }

    private int getCode(){
        Random rNo = new Random();
        final int code = rNo.nextInt((99999 - 10000) + 1) + 10000;
        return code;
    }
}
