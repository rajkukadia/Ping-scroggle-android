package edu.neu.madcourse.raj__kukadia.ping;


import android.app.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;


public class MyContactsActivity extends Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts_ping);


    }


 void onClickViewListiner(int position,ContactUser contactUser){
        FriendsFragment mFriendsFragment= (FriendsFragment) getFragmentManager().findFragmentById(R.id.fragmentFriends);
        if(mFriendsFragment!=null)mFriendsFragment.onClickViewListiner(position,contactUser);
    }



}




