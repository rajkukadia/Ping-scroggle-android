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

import edu.neu.madcourse.raj__kukadia.R;

public class UserInformationActivity extends Activity implements View.OnClickListener {

    private SharedPreferences firstUser = null;
    private Button jumpIn;
    private EditText phoneNumberArea;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstUser = getSharedPreferences("checkFirstUser", MODE_PRIVATE);

        if(firstUser.getBoolean("firstuser", true)){
            handleFirstUser();
        }else{
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
        firstUser.edit().putBoolean("firstuser", false).commit();
startActivity(new Intent(UserInformationActivity.this, PingHomeScreenActivity.class));
    }
}
