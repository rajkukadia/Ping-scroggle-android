package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

import edu.neu.madcourse.raj__kukadia.R;

public class OnlineOfflineActivity extends Activity{


    private Button OnlineFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_online_offline);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Play with ?");
        titleName.setTextSize(20);

        LinearLayout OnlineFriendList = (LinearLayout)(findViewById(R.id.online_friend_list));
        OnlineFriendList.removeAllViews();

        int size = MultiPlayerHomePageActivity.OnlineFriends.size();
            Iterator s = MultiPlayerHomePageActivity.OnlineFriends.entrySet().iterator();
            while(s.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) s.next();


                Button button = new Button(this);
                String friendName = MultiPlayerHomePageActivity.OnlineFriends.get(pair.getKey());
                button.setText(friendName);
                OnlineFriendList.addView(button);

                s.remove();

            }
    }
}