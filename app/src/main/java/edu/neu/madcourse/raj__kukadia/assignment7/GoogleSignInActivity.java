package edu.neu.madcourse.raj__kukadia.assignment7;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import edu.neu.madcourse.raj__kukadia.MainActivity;
import edu.neu.madcourse.raj__kukadia.R;

import static edu.neu.madcourse.raj__kukadia.assignment5.ScroggleAssignment5.PREF_RESTORE;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int SOME_REQUEST_CODE = 1;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    public GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "GoogleSignInActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Boolean allowed = false;
    public static Boolean isSignOut = false;
    public static HashMap<Integer, String> AllUserList = new HashMap<Integer, String>();
    private int userNumber = 0;
    private int i = 1;
    private Thread loadTheRemainingDictionaryThread;
    private boolean transfer;
    private String gameID;
    private String userOne;
    private String userTwo;
    private String notifier;
    private String gameState;
    private String turns;
    private String gameOver;
    private DatabaseReference mRootRef;
    private boolean isPresent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);
        //isSignOut=false;
        transfer = false;
        mRootRef = FirebaseDatabase.getInstance().getReference();
          isPresent = false;

       if(getIntent()!=null) {
           Bundle b = new Bundle();
            b = getIntent().getExtras();
           Log.d("Comes ", "before");
           if(b!=null) {
               Log.d("Comes", "after");
               gameID = b.getString("GameKey");
               userOne = b.getString("userOne");
               userTwo = b.getString("userTwo");
               notifier = b.getString("notifier");
               turns = b.getString("turns");
               gameOver = b.getString("gameOver");

               transfer = true;
           }
        }

        signInButton = (SignInButton) findViewById(R.id.googlesigninbutton);

        setTitle("Sign In");
        mAuth = FirebaseAuth.getInstance();


        loadTheRemainingDictionaryThread = new Thread(new DictionaryLoader());
       // loadTheRemainingDictionaryThread.start();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("kyocera", "0");
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d("kyocera", "1");
                    // store list of all the users who had logged any time in here
                    String token = FirebaseInstanceId.getInstance().getToken();
                    User user = new User(firebaseAuth.getCurrentUser().getDisplayName().toString(), firebaseAuth.getCurrentUser().getEmail().toString(), token);
                    String logMessage = "Token: " + token;
                    Log.d("Current token:", logMessage);
                    Toast.makeText(GoogleSignInActivity.this, logMessage, Toast.LENGTH_SHORT).show();

                    String userID = firebaseAuth.getCurrentUser().getUid();

                    //  if(!AllUserList.containsValue(firebaseAuth.getCurrentUser().getDisplayName())){
                    //
                    // AllUserList.put(++userNumber, firebaseAuth.getCurrentUser().getDisplayName());}
                    Log.d("Current User Number: ", String.valueOf(userNumber));

                    writeNewUser(user, userID);
                    Log.d("kyocera", "2");
                    writeUserScoreToFireBase();

                    if(transfer){
                        Intent intent = new Intent(GoogleSignInActivity.this, ScroggleMultiplayerAsyncActivity.class);
                        intent.putExtra("GameKey", gameID);
                        intent.putExtra("userOne", userOne);
                        intent.putExtra("userTwo", userTwo);
                        intent.putExtra("notifier", notifier);
                        intent.putExtra("turns", turns);
                        intent.putExtra("gameOver", gameOver);
                        startActivity(intent);
                        transfer = false;

                    }else {

                        Intent intent = new Intent(GoogleSignInActivity.this, MultiPlayerHomePageActivity.class);
                        Log.d("kyocera", "3");
                        startActivity(intent);
                        Log.d("kyocera", "4");
                    }
                } else {

                    // startActivity(new Intent(GoogleSignInActivity.this, MultiPlayerHomePageActivity.class));
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void writeUserScoreToFireBase(){
        final DatabaseReference r = mRootRef.child("scoreBoard");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                        if (d.getKey().equals(mAuth.getCurrentUser().getDisplayName().toString())) {
                            isPresent = true;
                            break;
                        }


                    }
                    if (!isPresent) {
                        r.child(mAuth.getCurrentUser().getDisplayName().toString()).setValue("0");
                        isPresent=false;
                    }
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void writeNewUser(User user, String userID) {

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("All Users").child(userID).setValue(user);


        DatabaseReference r = mRootRef.child("All Users");
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

                            if (!AllUserList.containsValue(finalvalue.getValue().toString())) {
                                AllUserList.put(i++, finalvalue.getValue().toString());

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


    private void signIn() {
//isSignOut=false;
        //Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
        //      false, null, null, null, null);

        //  startActivityForResult(intent, SOME_REQUEST_CODE);
      //  mAuth.signOut();
        if (mGoogleApiClient.isConnected()) {
           Log.d("Checked ", "in the sign in");
          Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
/*
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOME_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
           Parcelable pa =   data.getParcelableExtra(accountName);

            firebaseAuthWithGoogle(acc);
            Log.d(accountName, "checkkk");


           /* GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...

                  }

            }
        }
*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
Log.d("onActivutyResult", "1");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("onActivutyResult", "2");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(result.getStatus().toString(), "check");
            int statusCode = result.getStatus().getStatusCode();
            Log.d("statuscde", String.valueOf(statusCode));
            if (result.isSuccess()) {
                Log.d("onActivutyResult", "3");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {


                Log.d("onActivutyResult", "4");
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.d("KYOCERS", "auth");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //String savedState = getPreferences(MODE_PRIVATE)
        //      .getString(PREF_RESTORE, null);
        // if (savedState != null) {
        //   putState(savedState);
        //}
        //allowed=false;

        // if(mGoogleApiClient.isConnected()&&isSignOut){
        // mAuth.signOut();
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);

     //   mAuth.addAuthStateListener(mAuthListener);
        // isSignOut=false;
        // }
    }

    public void putState(String savedState) {
        String[] fields = savedState.split(",");
        int index = 0;

        userNumber = Integer.parseInt(fields[index++]);
        int size = 0;
        if (!fields[index].equals("null")) {
            size = Integer.parseInt(fields[index++]);
        }

        // AllUserList.clear();

        for (int i = 1; i <= size; i++) {
            AllUserList.remove(i);
            AllUserList.put(i, fields[index++]);
        }

    }


    public String getState() {
        StringBuilder builder = new StringBuilder();
        builder.append(userNumber);
        builder.append(",");
        builder.append(AllUserList.size());
        builder.append(",");

        for (int i = 1; i <= AllUserList.size(); i++) {
            builder.append(AllUserList.get(i));
            builder.append(",");
        }
        return builder.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //  String savedState = getState();
        // getPreferences(MODE_PRIVATE).edit()
        //       .putString(PREF_RESTORE, savedState)
        //     .commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class DictionaryLoader implements Runnable {


        InputStream is8;
        InputStream is13;
        InputStream is14;
        InputStream is15;
        InputStream is16;

        DataInputStream din13;
        DataInputStream din14;
        DataInputStream din15;
        DataInputStream din16;

        BufferedReader br;
        InputStreamReader inReader;

        @Override
        public void run() {
            createStreams();
            Message mg = Message.obtain();
            mg.arg1 = 0;

            // handleLoadingDictionary.sendMessage(mg);
            //  handleLoadingDictionary;


            setelevenfile();
            settwelvefile();
            setthirteenfile();
            setfourteenfile();
        }

        private void setelevenfile() {
            for (int data = 0; data < 45675; data++) {
                Long d = null;
                try {
                    d = din13.readLong();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.elevenWords.put(d, d);

            }

            try {
                din13.close();
                is13.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void settwelvefile() {
            for (int data = 0; data < 35470; data++) {
                Long d = null;
                try {
                    d = din14.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.twelveWords.put(d, d);

            }

            try {
                din14.close();
                is14.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void setthirteenfile() {
            for (int data = 0; data < 25593; data++) {
                Long d = null;
                try {
                    d = din15.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    MainActivity.thirteenWords.put(d, d);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }

            try {
                din15.close();
                is15.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setfourteenfile() {
            for (int data = 0; data < 17591; data++) {
                Long d = null;
                try {
                    d = din16.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.fourteenWords.put(d, d);

            }

            try {
                din16.close();
                is16.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void createStreams() {
            is13 = getResources().openRawResource(R.raw.elevenwords);
            is14 = getResources().openRawResource(R.raw.twelvewords);
            is15 = getResources().openRawResource(R.raw.thirteenwords);
            is16 = getResources().openRawResource(R.raw.fourteenwords);

            din13 = new DataInputStream(is13);
            din14 = new DataInputStream(is14);
            din15 = new DataInputStream(is15);
            din16 = new DataInputStream(is16);

        }

    }
}
