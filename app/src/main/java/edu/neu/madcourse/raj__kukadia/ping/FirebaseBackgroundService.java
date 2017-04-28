package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.raj__kukadia.R;

/**
 * Created by rajku on 4/23/2017.
 */

public class FirebaseBackgroundService extends Service {


    private ValueEventListener handler;
    private DatabaseReference reference;
    private String latestActivity;
    private boolean serviceStopped;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceStopped = false;
        FirebaseApp.initializeApp(this);

        reference = FirebaseDatabase.getInstance().getReference("Ping").child("recent");

        reference.addValueEventListener(new ValueEventListener() {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
      long count =  dataSnapshot.getChildrenCount();
        for(DataSnapshot finalVal : dataSnapshot.getChildren()){
            for(DataSnapshot middle : finalVal.getChildren()) {
                for (DataSnapshot d : middle.getChildren()) {
                    if (d.getKey().equals("activityname")) latestActivity = middle.getKey().toString()+": "+(String) d.getValue();
                }
            }
        }
if(!serviceStopped)
postNotif(latestActivity);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

    }




    public void postNotif(String notifString) {

        Intent intent = new Intent(this, PingHomeScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //  .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("PING")
                .setContentText(notifString)
                .setSmallIcon(R.drawable.ping_icon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    @Override
    public void onDestroy() {
        serviceStopped = true;
        Toast.makeText(this, "Stopping", Toast.LENGTH_LONG).show();
        super.onDestroy();


    }
}
