package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.neu.madcourse.raj__kukadia.MainActivity;
import edu.neu.madcourse.raj__kukadia.R;

public class MultiPlayerHomePageActivity extends Activity {

    private DatabaseReference mRootRef;
    private Button mLogout;
    private Button mStartMultiplayerGame;
    private Button MyFriends;
    private Button ack;
    private Button scoreBoard;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    public static HashMap<String, String> OnlineFriends = new HashMap<String, String>();
    public static HashMap<String, String> OfflineFriends = new HashMap<String, String>();
    private boolean isPresent = false;


    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_multiplayer_homepage);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
        Log.d("kyocera", "5");
        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("Two Player Game");
        titleName.setTextSize(20);



        MyFriends = (Button) findViewById(R.id.myfriends_button);
        ack = (Button) findViewById(R.id.ack_button_multiplayer);
        scoreBoard = (Button) findViewById(R.id.score_board_button);


        mStartMultiplayerGame = (Button) findViewById(R.id.startmultiplayer_button);

        MyFriends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiPlayerHomePageActivity.this, MyFriendsActivity.class));
            }
        });

       mStartMultiplayerGame.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MultiPlayerHomePageActivity.this, OnlineOfflineActivity.class));
           }
       });

        ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiPlayerHomePageActivity.this, MultiplayerAcknowledgementActivity.class));
            }
        });
        scoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiPlayerHomePageActivity.this, ScoreBoardActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        //Write this user to database

        mRootRef = FirebaseDatabase.getInstance().getReference();


            //mRootRef.child("Users");



      //  Log.d("Current user name: ", mAuth.getCurrentUser().getDisplayName().toString());
       // Log.d("Current user name: ", mAuth.getCurrentUser().getEmail().toString());
       // Log.d("Current user name: ", mAuth.getCurrentUser().getToken(true).toString());
       // Log.d("Current user name: ", mAuth.getCurrentUser().getUid().toString());




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                }else {

                }
            }
        };

        mLogout = (Button) findViewById(R.id.logout_button);
        mLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userID = mAuth.getCurrentUser().getUid().toString();
                DatabaseReference r = mRootRef.child("active users").child(userID);
                String name = mAuth.getCurrentUser().getDisplayName().toString();
                String emailID = mAuth.getCurrentUser().getEmail().toString();
                //User user = new User(name, emailID);

              /*  int KeyToRemove = 0;
                Iterator s = OnlineFriends.entrySet().iterator();
                while(s.hasNext()){
                    HashMap.Entry pair  = (HashMap.Entry)s.next();
                    if(pair.getValue().equals(name)){
                        KeyToRemove = (int) pair.getKey();
                    }
                    s.remove();
                } */
                OnlineFriends.remove(name);
                //OfflineFriends.put(name, name);
               r.removeValue();
                mAuth.signOut();
                    //finish();
                //if(GoogleSignInActivity.mGoogleApiClient.isConnected()){
               // Auth.GoogleSignInApi.signOut(GoogleSignInActivity.mGoogleApiClient);}



              //  GoogleSignInActivity.mAuth.signOut();
                 //if(GoogleSignInActivity.mGoogleApiClient.isConnected()){
                  //Auth.GoogleSignInApi.signOut(GoogleSignInActivity.mGoogleApiClient);//}


                //finish();
             //GoogleSignInActivity.isSignOut=true;

               // finish();
                //Auth.GoogleSignInApi.signOut(GoogleSignInActivity.mGoogleApiClient);
              /*.setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                // updateUI(null);
                            }
                        });
*/
            }
        });

      //  writeUserScoreToFireBase();
    }


    private void writeUserScoreToFireBase(){
      final DatabaseReference r = mRootRef.child("scoreBoard");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser() != null) {
                        if (d.getKey().equals(mAuth.getCurrentUser().getDisplayName().toString())) {
                            isPresent = true;
                            break;
                        }

                        if (!isPresent) {
                            r.child(mAuth.getCurrentUser().getDisplayName().toString()).setValue("0");
                        }
                    }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser()!=null) {
            //write active users to database
            String token = FirebaseInstanceId.getInstance().getToken();
            writeNewUser(mAuth.getCurrentUser().getUid().toString(), mAuth.getCurrentUser().getDisplayName().toString(), mAuth.getCurrentUser().getEmail().toString(), token);

        }




    }

    private  void writeNewUser(String userID, String name, String emailID, String token) {
        User user = new User(name, emailID, token);
        mRootRef.child("active users").child(userID).setValue(user);


        //set online users hashMap

        DatabaseReference r = mRootRef.child("active users");
        r.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Arrived.", "here");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("Arrived.", "here2");

                    for (DataSnapshot finalvalue : child.getChildren()) {
                        Log.d("Arrived.", "here3");

                        if (finalvalue.getKey().equals("username")) {
                            Log.d("Arrived.", "here4");

                            if (!OnlineFriends.containsValue(finalvalue.getValue().toString())) {
                                OnlineFriends.put(finalvalue.getValue().toString(), finalvalue.getValue().toString());
                           }
                        } else {
                            // OnlineFriends.clear();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Set offline users HashMap

        DatabaseReference r1 = mRootRef.child("All Users");
        r1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Arrived.", "here");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("Arrived.", "here2");

                    for (DataSnapshot finalvalue : child.getChildren()) {
                        Log.d("Arrived.", "here3");

                        if (finalvalue.getKey().equals("username")) {
                            Log.d("Arrived.", "here4");

                            if (!OfflineFriends.containsValue(finalvalue.getValue().toString())&&!OnlineFriends.containsValue(finalvalue.getValue().toString())) {
                                OfflineFriends.put(finalvalue.getValue().toString(), finalvalue.getValue().toString());
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

   @Override
    public void onStop(){
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    }

