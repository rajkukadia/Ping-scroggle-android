package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.neu.madcourse.raj__kukadia.R;

public class PreInitializingGameActivity extends Activity {

   private static String gameID;
    private DatabaseReference mRootRef;
    private HashMap<Integer, String> gameIDMap = new HashMap<Integer, String>();
    private Button Start;
    private FirebaseAuth mAuth;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_initializing_game);
        Start = (Button) findViewById(R.id.pre_initializing_button);


        mRootRef = FirebaseDatabase.getInstance().getReference();

        gameID = fetchGameID();
       // Log.d(gameID)

      //  dialog.hide();
        //Log.d("gameID at pre", gameIDMap.get(1));
        mAuth = FirebaseAuth.getInstance();
Start.setOnClickListener(new View.OnClickListener(){

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(PreInitializingGameActivity.this, ScroggleMultiplayerActivity.class);
        Log.d("gameID at pre", gameIDMap.get(1));
        intent.putExtra("GameKey", gameIDMap.get(1));
        intent.putExtra("CallingActivity", PreInitializingGameActivity.class.toString());
        intent.putExtra("username", mAuth.getCurrentUser().getDisplayName().toString());
        DatabaseReference r = mRootRef.child("SynchronousGames").child(gameID);
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.getKey().equals("timeup")){
                        if(d.getValue().equals("no")){
                            startActivity(intent);
                          //  startActivity(intent);
                            setAgreedYes();
                            finish();
                        }
                        else{
                            final Dialog alertDialog = new Dialog(PreInitializingGameActivity.this);
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.setContentView(R.layout.sorry_timeup);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Button  coolOk = (Button)alertDialog.findViewById(R.id.OK_timeup);
                            coolOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.cancel();
                                   // mRootRef.child("SynchronousGames").removeValue();
                                    finish();
                                }
                            });
                            alertDialog.show();

                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    //    mRootRef.child("SynchronousGames").child(gameIDMap.get(1)).child("userB").setValue(mAuth.getCurrentUser().getDisplayName().toString());



    }
});

    }



    private String fetchGameID(){
        Log.d("comes here", "?");

        mRootRef =  FirebaseDatabase.getInstance().getReference();

        DatabaseReference r = mRootRef.child("active users");

        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("comes here", "??");


                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Log.d("comes here", "???");

                    for(DataSnapshot finalsnap : d.getChildren()){
                        Log.d("comes here", "?");

                        if(finalsnap.getKey().equals("opponent")){
                            gameID = finalsnap.getValue().toString();
                            gameIDMap.put(1, gameID);
                            Log.d("gameID at prue", gameID);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return gameID;
    }

    private void setAgreedYes(){
        Log.d("aggreed here", "?");

        DatabaseReference dr = mRootRef.child("SynchronousGames");

       dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("aggreed here", "??");

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Log.d("aggreed here", "???");
                    if(d.getKey().equals(gameID)){
                        Log.d("aggreed here", "????");
                        mRootRef.child("SynchronousGames").child(gameID).child("aggreed").setValue("yes");
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}