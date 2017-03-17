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
import java.util.Random;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleStatusAssignment5;

public class WaitingForOpponentActivity extends Activity{


    TextView FriendList;
    private int t = 10;
    private TextView waiting_for_opponent_timer;
    private Handler mHandler = new Handler();
    private DatabaseReference mRootRef;
    private Boolean firsttime = true;
    public static Boolean finishTheGame = false;
    private final int MAX_LENGTH = 20;
    private String gameID;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firsttime =true;
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_waiting_for_opponent);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView)findViewById(R.id.title_name);
        waiting_for_opponent_timer = (TextView) findViewById(R.id.waiting_for_opponent_timer);
        titleName.setText("Waiting for Opponent");

        titleName.setTextSize(20);

        Bundle b = getIntent().getExtras();
        String user = b.getString("UserName");

        gameID = generateRandomNumber();

        writeGameIDToFireBase(gameID, user);

        mHandler.postDelayed(mRunnable, 1000);


    }


    private void writeGameIDToFireBase(final String gameID, final String user){
        mRootRef = FirebaseDatabase.getInstance().getReference();

        GameInfo gi = new GameInfo(null, "no", null);
       // mRootRef.child("SynchronousGames").child("GameIDs").removeValue();
        mRootRef.child("SynchronousGames").child(gameID).setValue(gi);

        mRootRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
Log.d("gameID2", gameID);


                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Log.d("gameID3", gameID);

                    if(d.getKey().equals("active users")){
                        for(DataSnapshot child : d.getChildren()){
                            Log.d("gameID4", gameID);

                            for(DataSnapshot finalchild : child.getChildren()){
                                Log.d("gameID5", gameID);

                                if(finalchild.getKey().equals("username")){
                                    Log.d("gameID6", gameID);


                                    if(finalchild.getValue().equals(user)){
                                        Log.d("gameID7", gameID);
                                        Log.d("username", user);

                                         mRootRef.child("active users").child(child.getKey()).child("opponent").setValue(gameID);
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
    }







    private String generateRandomNumber(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar1;
        char tempChar2;
        char tempChar3;
        for (int i = 0; i < randomLength; i++){
            tempChar1 = (char) (generator.nextInt(57-48+1) + 48);
            tempChar2= (char) (generator.nextInt(90-65+1) + 65);
            tempChar3 = (char) (generator.nextInt(122-97+1) + 97);
            randomStringBuilder.append((tempChar1+tempChar2+tempChar3));
        }
        Log.d("gameID1", randomStringBuilder.toString());
        return randomStringBuilder.toString();
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

                        if(child.getKey().equals("SynchronousGames")){
                            for(DataSnapshot d : child.getChildren()){
                                if(d.getKey().equals("aggreed")){
                                    if(d.getValue().equals("yes")){
                                       mHandler.removeCallbacks(mRunnable);

                                        if(firsttime){
                                            setGamePlayingYes();
                                            startActivity(new Intent(WaitingForOpponentActivity.this, ScroggleMultiplayerActivity.class));
                                            setAggreedNo();
                                            firsttime = false;
                                        }
                                        else{
                                            //mRootRef.child("GameData").child("gamePlaying").setValue("no");
                                            //finishTheGame = true;
                                           // finish();
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


    private void setGamePlayingYes(){

mRootRef.child("SynchronousGames")
        .child(gameID)
        .child("gamePlaying")
        .runTransaction(new Transaction.Handler(){

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue("yes");
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void setAggreedNo(){

        mRootRef.child("SynchronousGames")
                .child(gameID)
                .child("aggreed")
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