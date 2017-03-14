package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import edu.neu.madcourse.raj__kukadia.R;

public class MyFriendsActivity extends Activity{


    TextView FriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_my_friends);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Connections possible");

        titleName.setTextSize(20);

        FriendList = (TextView) findViewById(R.id.friend_list);

        for(int i = 1; i<=(GoogleSignInActivity.AllUserList.size());i++){
          FriendList.append((GoogleSignInActivity.AllUserList.get(i))+"\n");
        }
    }
}