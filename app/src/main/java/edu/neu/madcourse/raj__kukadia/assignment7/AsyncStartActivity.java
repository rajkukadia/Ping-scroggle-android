package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import edu.neu.madcourse.raj__kukadia.R;

public class AsyncStartActivity extends Activity {

   private static String gameIDForAsync;
    private DatabaseReference mRootRef;
    private HashMap<Integer, String> gameIDMap = new HashMap<Integer, String>();
    private Button Start;
    private FirebaseAuth mAuth;
    private final int MAX_LENGTH = 20;
    private boolean Once = true;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_start);
        Start = (Button) findViewById(R.id.async_start_button);
        Once = true;
        Bundle b = getIntent().getExtras();
        final String user = b.getString("UserName");

        gameIDForAsync = generateGameIDForAsync();

        writeAsyncGameIDToFireBase(gameIDForAsync, user);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();


        Start.setOnClickListener(new View.OnClickListener(){

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AsyncStartActivity.this, ScroggleMultiplayerAsyncActivity.class);
        Log.d("gameID at pre", gameIDForAsync);
        intent.putExtra("GameKey", gameIDForAsync);
        intent.putExtra("CallingActivity", AsyncStartActivity.class.toString());
        intent.putExtra("username", mAuth.getCurrentUser().getDisplayName().toString());
        intent.putExtra("opponent", user);
        startActivity(intent);

        finish();
    //    mRootRef.child("SynchronousGames").child(gameIDMap.get(1)).child("userB").setValue(mAuth.getCurrentUser().getDisplayName().toString());



    }
});


    }

    private String generateGameIDForAsync(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH)+1;
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

    private void writeAsyncGameIDToFireBase(final String gameID, final String user){
        mRootRef = FirebaseDatabase.getInstance().getReference();

        GameInfo gi = new GameInfo(null, "no", null, null, null, null);
        // mRootRef.child("SynchronousGames").child("GameIDs").removeValue();
        mRootRef.child("AsynchronousGames").child(gameID).setValue(gi);

        mRootRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("gameID2", gameID);


                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Log.d("gameID3", gameID);

                    if(d.getKey().equals("All Users")){
                        for(DataSnapshot child : d.getChildren()){
                            Log.d("gameID4", gameID);

                            for(DataSnapshot finalchild : child.getChildren()){
                                //   Log.d("gameID5", gameID);

                                if(finalchild.getKey().equals("username")){
                                    //   Log.d("gameID6", gameID);


                                    if(finalchild.getValue().equals(user)){
                                        //Log.d("gameID7", gameID);
                                        // Log.d("username", user);
                                        if(Once) {
                                            mRootRef.child("All Users").child(child.getKey()).child("opponent").setValue(gameID);
                                            Once = false;
                                        }
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


}