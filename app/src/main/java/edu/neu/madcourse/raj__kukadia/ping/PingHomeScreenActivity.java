package edu.neu.madcourse.raj__kukadia.ping;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.raj__kukadia.R;

public class PingHomeScreenActivity extends Activity implements View.OnClickListener {


    private Button showMyContacts;
    private Button showActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_ping);
        showMyContacts = (Button) findViewById(R.id.show_my_contacts);
        showActivities = (Button) findViewById(R.id.search_activities);
        showMyContacts.setOnClickListener(this);
        showActivities.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.show_my_contacts){
startActivity(new Intent(PingHomeScreenActivity.this, MyContactsActivity.class));}
        if(v.getId()==R.id.search_activities){
            startActivity(new Intent(PingHomeScreenActivity.this, MySearchActivity.class));}

    }
    }

