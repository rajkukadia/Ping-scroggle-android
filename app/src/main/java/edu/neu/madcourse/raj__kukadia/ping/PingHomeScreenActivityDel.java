package edu.neu.madcourse.raj__kukadia.ping;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;

public class PingHomeScreenActivityDel extends AppCompatActivity implements View.OnClickListener {


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
startActivity(new Intent(PingHomeScreenActivityDel.this, MyContactsActivity.class));}
        if(v.getId()==R.id.search_activities){
            startActivity(new Intent(PingHomeScreenActivityDel.this, MySearchActivity.class));}

    }
    public void onClickViewListiner(int position,ContactUser contactUser){
        FriendsFragment mFriendsFragment= (FriendsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFriends);
        if(mFriendsFragment!=null)mFriendsFragment.onClickViewListiner(position,contactUser);
    }
    public void updateTargetListView(){
        TargetsFragment mFriendsFragment= (TargetsFragment) getSupportFragmentManager().findFragmentById(R.id.TargetFriends);
        mFriendsFragment.contactFunction();
    }
    }

