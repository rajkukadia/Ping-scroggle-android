package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleStatusAssignment5;

public class WaitingForOpponentActivity extends Activity{


    TextView FriendList;
    private int t = 10;
    private TextView waiting_for_opponent_timer;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_waiting_for_opponent);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView)findViewById(R.id.title_name);
        waiting_for_opponent_timer = (TextView) findViewById(R.id.waiting_for_opponent_timer);
        titleName.setText("Waiting for Opponent");

        titleName.setTextSize(20);

        mHandler.postDelayed(mRunnable, 1000);


    }



    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            waiting_for_opponent_timer.setText(String.valueOf(t));
            t--;




            if(t==0){
                waiting_for_opponent_timer.setText("Time up");
                mHandler.removeCallbacks(mRunnable);
            }

    else {
        mHandler.postDelayed(mRunnable, 1000);
    }
}


};





}