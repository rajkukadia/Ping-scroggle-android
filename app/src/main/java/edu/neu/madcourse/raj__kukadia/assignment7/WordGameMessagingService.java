package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.neu.madcourse.raj__kukadia.MainActivity;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.MySearchActivity;

public class WordGameMessagingService extends FirebaseMessagingService {

    private static final String KEY_TEXT_REPLY = "key_text_reply";

    private DatabaseReference mRootRef;
    private HashMap<Integer, String> gameMode = new HashMap<Integer, String>();
    private FirebaseAuth mAuth;

    private static final String TAG = WordGameMessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String gameID="";
        String userOne="";
        String userTwo="";
        String notifier="";
        String turns="";
        String ping="";
        String gameOver ="";
        mRootRef = FirebaseDatabase.getInstance().getReference();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map jData = remoteMessage.getData();
            Log.d("recee", "asda11");
            ping = jData.get("ping").toString();

          // gameID = jData.get("GameKey").toString();
            // userOne = jData.get("userOne").toString();
             //userTwo = jData.get("userTwo").toString();
            //notifier = jData.get("notifier").toString();
            //turns = jData.get("turns").toString();
            //gameOver = jData.get("gameOver").toString();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("recee", "asda1111");

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            final String gameKey = gameID;
            final String userA = userOne;
            final String userB = userTwo;
            final String mNotifier = notifier;
            final String mTurns = turns;
            final String mgameOver = gameOver;

            if(ping.equals("open")){
                Log.d("recee", "yoo");

                sendNotification(remoteMessage.getNotification().getBody());
            }

         /*   mRootRef = FirebaseDatabase.getInstance().getReference();


            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        if(child.getKey().equals("gameMode")){
                           gameMode.put(1,child.getValue().toString());

                            if(gameMode.get(1).equals("offline")){
                                if(remoteMessage.getData().size()>0){
                                    sendNotificationAsyncGamePlay(gameKey, userA, userB, mNotifier, mTurns, mgameOver);
                                }else {
                                    sendNotificationAsyncGameStart(remoteMessage.getNotification().getBody());
                                }
                            }
                            if(gameMode.get(1).equals("online")){
                                sendNotificationSynchrnous(remoteMessage.getNotification().getBody());

                            }

                        }

                                }




                        }




                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

*/

        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotificationAsyncGameStart(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              //  .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Scroggle")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void sendNotificationAsyncGamePlay(String gameID,String userOne, String userTwo, String notifier, String mTurns, String gameOver) {

        mAuth = FirebaseAuth.getInstance();
        PendingIntent pendingIntent;

        if(mAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, ScroggleMultiplayerAsyncActivity.class);
            intent.putExtra("GameKey", gameID);
            intent.putExtra("userOne", userOne);
            intent.putExtra("userTwo", userTwo);
            intent.putExtra("notifier", notifier);
            intent.putExtra("turns", mTurns);
            intent.putExtra("gameOver", gameOver);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else{
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            intent.putExtra("GameKey", gameID);
            intent.putExtra("userOne", userOne);
            intent.putExtra("userTwo", userTwo);
            intent.putExtra("notifier", notifier);
            intent.putExtra("turns", mTurns);
            intent.putExtra("gameOver", gameOver);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //  .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Scroggle")
                .setContentText("Your turn")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }


    private void sendNotificationSynchrnous(String messageBody){

        Intent intent = new Intent(this, PreInitializingGameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //  .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Tap to play?")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }



    private void sendNotification(String messageBody){

        Log.d("recee", "asda");
        Intent intent = new Intent(this, MySearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //  .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("PING")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }




}
