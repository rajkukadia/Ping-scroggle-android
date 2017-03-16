package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import edu.neu.madcourse.raj__kukadia.R;

public class OnlineOfflineActivity extends Activity{

    private static final String TAG = OnlineOfflineActivity.class.getSimpleName();
    private DatabaseReference mRootRef;
    private String tokenOnline;
    private String tokenOffline;
    private static final String SERVER_KEY = "key=AAAAIJKsPeE:APA91bHkUeOjkpMKSV9gmCv1kzJEadSJGPjaKSA5xjI-R2waz2RJRv1zqcHz-t4I9XSrB5HaCLNLQSW0TTvXkhkVHTDn0FFCOZop-2lP9cTWG1acrTYGxg9WuJjFygeQaLo7URrr9sQo";
    public static String gameMode = " ";
    public static String user_one;
    public static String user_two;
    private FirebaseAuth mAuth;
    private String a,b;
    public static final String PREF_RESTORE = "pref_restore";
    private String usersPlaying;
    private static int counter = 0;
    private static HashMap<Integer, String> userOneTwo = new HashMap<Integer, String>();
    private Boolean broke = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_online_offline);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Play with ?");
        titleName.setTextSize(20);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("GameData").child("gamePlaying").setValue("no");


        final LinearLayout OnlineFriendList = (LinearLayout)(findViewById(R.id.online_friend_list));
        OnlineFriendList.removeAllViews();

        int size = MultiPlayerHomePageActivity.OnlineFriends.size();
            Iterator s = MultiPlayerHomePageActivity.OnlineFriends.entrySet().iterator();
            while(s.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) s.next();


                Button buttonOnline = new Button(this);
                final String friendName = MultiPlayerHomePageActivity.OnlineFriends.get(pair.getKey());
                buttonOnline.setText(friendName);
                OnlineFriendList.addView(buttonOnline);

                buttonOnline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("onClick, Online", "pressed");


                        mRootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference r1 = mRootRef.child("All Users");

                        r1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Log.d("Arrived.", "hereOnline");
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Log.d("Arrived.", "here2On");

                                    for (DataSnapshot finalvalue : child.getChildren()) {
                                        Log.d("Arrived.", "here3On");

                                        if (finalvalue.getValue().equals(friendName)) {



                                            String user = friendName;

                                            mAuth = FirebaseAuth.getInstance();
                                            String user1= mAuth.getCurrentUser().getDisplayName();
                                            mRootRef = FirebaseDatabase.getInstance().getReference();
                                            mRootRef.child("TwoUsers").setValue(user+"_"+user1);

                                             //   setData(user, user1);

                                         //   thisIsUserTwo(user, user1);


                                            //Log.d(user_one,"User One");
                                            //Log.d(user_two, "User Two");
                                            Log.d("Arrived.", "here4On");

                                            Iterable<DataSnapshot> i = child.getChildren();

                                            Iterator values = i.iterator();
                                            while(values.hasNext()){


                                                DataSnapshot d = (DataSnapshot) values.next();

                                                if(d.getKey().equals("token")){
                                                    tokenOnline =  d.getValue().toString();
                                                    Log.d("Token req is: ", tokenOnline);
                                                    pushNotification(0, tokenOnline);
                                                    startActivity(new Intent(OnlineOfflineActivity.this, WaitingForOpponentActivity.class));
                                                    break;
                                                }
                                                // values.remove();
                                            }


                                        }

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                       // gameMode = "online";
                       // r1.child("yell");
                      //  if(gameMode.equals("online")) {
                          //  pushNotification(0, tokenOnline);
                          //  startActivity(new Intent(OnlineOfflineActivity.this, WaitingForOpponentActivity.class));
                       // }else {
                       //     pushNotification(0, tokenOffline);
                       // }


                    }
                });

                s.remove();
               // Log.d(user_one,"User One");
                //Log.d(user_two, "User Two");


            }


        LinearLayout OfflineFriendList = (LinearLayout)(findViewById(R.id.offline_friend_list));
        OfflineFriendList.removeAllViews();

        Iterator s1 = MultiPlayerHomePageActivity.OfflineFriends.entrySet().iterator();
        while(s1.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) s1.next();


            Button buttonOffline = new Button(this);
            final String friendName = MultiPlayerHomePageActivity.OfflineFriends.get(pair.getKey());
            buttonOffline.setText(friendName);
            OfflineFriendList.addView(buttonOffline);


            buttonOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("onClick, Offline", "pressed");
            mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference r1 = mRootRef.child("All Users");
            r1.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("Arrived.", "hereOff");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("Arrived.", "here2Off");

                        for (DataSnapshot finalvalue : child.getChildren()) {
                            Log.d("Arrived.", "here3oFF");

                            if (finalvalue.getValue().equals(friendName)) {
                                Log.d("Arrived.", "here4Off");
                                Iterable<DataSnapshot> i = child.getChildren();

                                Iterator values = i.iterator();
                                while(values.hasNext()){


                                    DataSnapshot d = (DataSnapshot) values.next();

                                    if(d.getKey().equals("token")){
                                        tokenOffline =  d.getValue().toString();
                                        Log.d("Token req is: ", tokenOffline);
                                        pushNotification(0, tokenOffline);
                                        break;

                                    }
                                   // values.remove();

                                }



                            }

                        }

                    }
                }




                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                  //  gameMode = "offline";
                  //  if(gameMode.equals("online")) {
                       // pushNotification(0, tokenOnline);
                      //  startActivity(new Intent(OnlineOfflineActivity.this, WaitingForOpponentActivity.class));
                   // }else {
                  //      pushNotification(0, tokenOffline);
                  //  }

                }
            });
            s1.remove();


        }


        }

    private void  thisIsUserTwo(String user, String user1){
        user_two = user;
        user_one = user1;
        Log.d("checkkkkkk", "valiuesssssssssssssss");
        Log.d(user_two, user_one);

    }


    private void pushNotification(int i, String token) {
        final String final_token = token;
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(final_token);
            }
        }).start();
    }

    private void pushNotification(String token) {
   //     Log.d(user_one,"User One");
    //    Log.d(user_two, "User Two");
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // If sending to a single client
           // if(token!=null){
            //Log.d("finalllllll tokeennnn", token);}
            jPayload.put("to", token);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    Toast.makeText(OnlineOfflineActivity.this,resp,Toast.LENGTH_LONG);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


    public static void setData(String a, String b){
        userOneTwo.put(1, a);
        userOneTwo.put(2,b);
        OnlineOfflineActivity.user_one=a;
        OnlineOfflineActivity.user_two=b;
    }

    public static String getUserOne(){

        return userOneTwo.get(1);

    }

    public static String getUserTwo(){
        return userOneTwo.get(2);

    }

}