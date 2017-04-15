package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import edu.neu.madcourse.raj__kukadia.R;

public class UserInformationActivity extends Activity implements View.OnClickListener {

    private SharedPreferences firstUser = null;
    private Button jumpIn;
    private EditText phoneNumberArea;
    private TextView info;
    DatabaseReference mRootRef;
    private String token;
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

    private void  handleFirstUser(){
        setContentView(R.layout.activity_user_information_ping);
        jumpIn = (Button) findViewById(R.id.jump_in_button);
        phoneNumberArea = (EditText) findViewById(R.id.phone_number_area);
        info = (TextView) findViewById(R.id.enter_phone_number);
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
        phoneNumber = phoneNumberArea.getText().toString();

        firstUser.edit().putBoolean("firstuser", false).commit();
        firstUser.edit().putString("phonenumber", phoneNumber).commit();
        mRootRef.child("Ping").child("All Users").child(phoneNumberArea.getText().toString()).child("token").setValue(token);
        startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
    }
}
