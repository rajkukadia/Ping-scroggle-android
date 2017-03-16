package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Iterator;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleStatusAssignment5;

public class WaitingForOpponentActivity extends Activity{


    TextView FriendList;
    private int t = 10;
    private TextView waiting_for_opponent_timer;
    private Handler mHandler = new Handler();
    private DatabaseReference mRootRef;
    private Boolean firsttime = true;


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


            mRootRef = FirebaseDatabase.getInstance().getReference();

            mRootRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("Arrived.", "hereOnline");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("Arrived.", "here2On");

                        if(child.getKey().equals("GameData")){
                            for(DataSnapshot d : child.getChildren()){
                                if(d.getKey().equals("msgForOpponent")){
                                    if(d.getValue().equals("yes")){
                                       mHandler.removeCallbacks(mRunnable);
                                        mRootRef.child("GameData").child("gamePlaying").setValue("yes");
                                        if(firsttime){
                                        startActivity(new Intent(WaitingForOpponentActivity.this, ScroggleMultiplayerActivity.class));
                                        setMsgForOpponentNo();
                                        firsttime = false;}
                                        else{
                                            finish();
                                        }

                                    }
                                }
                            }
                        }








                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            if(t==0){
                waiting_for_opponent_timer.setText("Time up");
                mHandler.removeCallbacks(mRunnable);
            }

    else {
        mHandler.postDelayed(mRunnable, 1000);
    }
}


};


    private void setMsgForOpponentNo(){

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("GameData")
                .child("msgForOpponent")
                .runTransaction(new Transaction.Handler(){

                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        mutableData.setValue("no");
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });

    }



}