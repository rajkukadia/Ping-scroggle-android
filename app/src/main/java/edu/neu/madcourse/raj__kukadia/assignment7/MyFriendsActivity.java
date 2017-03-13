package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import edu.neu.madcourse.raj__kukadia.R;

public class MyFriendsActivity extends Activity{


    TextView FriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_my_friends);
        FriendList = (TextView) findViewById(R.id.friend_list);


        for(int i = 1; i<=(GoogleSignInActivity.AllUserList.size());i++){
          FriendList.append((GoogleSignInActivity.AllUserList.get(i))+"\n");
        }
    }
}