package edu.neu.madcourse.raj__kukadia.ping.persistent_model;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Date;
import java.util.Scanner;

import edu.neu.madcourse.raj__kukadia.ping.UserInformationActivity;
import edu.neu.madcourse.raj__kukadia.ping.applicatonlogic.myTasks;

/**
 * Created by Dharak on 4/12/2017.
 */

public class ContactUser implements Comparable<ContactUser>,myTasks {
    private String name;
    private String number;
    private String allowedNumber="1234567890";
    private boolean usesPing;
    private String token;
    private Button contactSearchButtonUpdate;
    private Button targetsButtonUpdate;
    private Button receivedButton;
    private String activity;
    private long milliseconds;// this actually milliseconds 1 January 1970, 00:00:00.000 UTC
    public String getName() {
        return name;
    }
    private long getCurretTime(){
        Date date=new Date();
        return date.getTime();
    }
    private boolean isActivityRecent(){
        if(getCurretTime()-milliseconds<=900000)return true;
        else return false;
    }
    public Button getContactSearchButtonUpdate() {
        return contactSearchButtonUpdate;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public boolean setActivityWithTime(String activity, long milliseconds){
        /*
        check whether current activity is given activity is recent previous activity
         */
      if(getActivity()==null||getMilliseconds()==0){
        setActivity(activity);
          setMilliseconds(milliseconds);
          return true;
      }
      else if(activity.length()>3&&getMilliseconds()<milliseconds){
          setActivity(activity);
          setMilliseconds(milliseconds);
          return true;
    }
    return false;
    }




    public void setContactSearchButtonUpdate(Button contactSearchButtonUpdate) {
        this.contactSearchButtonUpdate = contactSearchButtonUpdate;
    }

    public Button getTargetsButtonUpdate() {
        return targetsButtonUpdate;
    }

    public void setTargetsButtonUpdate(Button targetsButtonUpdate) {
        this.targetsButtonUpdate = targetsButtonUpdate;
    }

    public Button getReceivedButton() {
        return receivedButton;
    }

    public void setReceivedButton(Button receivedButton) {
        this.receivedButton = receivedButton;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getAllowedNumber() {
        return allowedNumber;
    }

    public void setAllowedNumber(String allowedNumber) {
        this.allowedNumber = allowedNumber;
    }

    public boolean isUsesPing() {
        return usesPing;
    }

    public void setUsesPing(boolean usesPing) {
        this.usesPing = usesPing;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Button getcontactSearchButtonUpdate() {
        return contactSearchButtonUpdate;
    }

    public void setButtonUpate(Button buttonUpate) {
        this.contactSearchButtonUpdate = buttonUpate;
    }

    ContactUser(String name, String number){
    this.name=name;
        try {

            this.number = numberFormat(number);
        }
        catch(Exception exe){
            return ;
        }
        if(name.contains("rak")){
            if(true){

            }
        }
        DatabaseReference newReference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users").child(this.number).child("token");
        newReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token1=dataSnapshot.getValue(String.class);
                if(token1!=null){
                    token=token1;
                    usesPing=true;

                    if(contactSearchButtonUpdate!=null){
                        Log.d("Inside","the the thing");
                        contactSearchButtonUpdate.setText("Ping");
                    }
                }
                else{
                    usesPing=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void removeContactSearchView(){
        contactSearchButtonUpdate=null;
    }


    @Override
    public String toString() {
        return getName();
    }

    private String numberFormat(String number){
/*
try not to put number less than 10 this handles data for greater than 10
 */
        String newNumber="";
        for(char c:number.toCharArray()){
            if((0<=Character.getNumericValue(c))&(10>Character.getNumericValue(c))){
                newNumber+=c;
            }
        }
        //newNumber.contentEquals()
        return newNumber.substring(newNumber.length()-10,newNumber.length());
    }

    @Override
    public int compareTo(@NonNull ContactUser o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean doTask() {
        pushInternetFCM();
        return true;
    }
    public void pushInternetFCM(){
                JSONObject jPayload = new JSONObject();

                JSONObject jNotification = new JSONObject();
                JSONObject jData=new JSONObject();
                try {
                    jPayload.put("to",token);
                    jData.put("ping", "open");
                    jNotification.put("title", "PING");
                    jNotification.put("body", "PING FROM YOUR BUDDY ");
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("click_action", "MySearchActivity");
                    jData.put("phonenumber", UserInformationActivity.phoneNumber);
                    //jPayload.put("notification", jNotification);

                    jPayload.put("notification", jNotification);
                    jPayload.put("data", jData);

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key=AAAAIJKsPeE:APA91bHkUeOjkpMKSV9gmCv1kzJEadSJGPjaKSA5xjI-R2waz2RJRv1zqcHz-t4I9XSrB5HaCLNLQSW0TTvXkhkVHTDn0FFCOZop-2lP9cTWG1acrTYGxg9WuJjFygeQaLo7URrr9sQo");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Send FCM message content.
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(jPayload.toString().getBytes());
                    outputStream.close();

                    // Read FCM response.
                    InputStream inputStream = conn.getInputStream();

                    final String resp = convertStreamToString(inputStream);
                    //Log.d("Sending notifcation3=",requesting);
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("notifcationf3", "run: " + resp);
                        }
                    });
                } catch (JSONException | IOException e) {
                    //Toast.makeText(getActivity(),"Ping unsuccesfully",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


    @Override
    public void OnTaskfailed() {

    }
}

