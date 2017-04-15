 package edu.neu.madcourse.raj__kukadia.ping;

 import android.app.Activity;
 import android.content.Intent;
 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Looper;
 import android.provider.ContactsContract;
 import android.speech.RecognizerIntent;
 import android.support.annotation.Nullable;
 import android.text.Editable;
 import android.text.TextWatcher;

 import android.util.Log;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.ListView;
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
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Scanner;

 import edu.neu.madcourse.raj__kukadia.R;

 public class MySearchActivity extends Activity {

     private ListView lv;
     private EditText e;
     private ImageButton googleMic;
     private String voiceText;
     private ArrayAdapter<String> adapter;
     ArrayList<String> activityList;
     private String phoneNumber;
     DatabaseReference reference;
     public static final int VOICE_RECOGNITION_REQUEST_CODE = 1230;

     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_my_search_ping);

         e = (EditText) findViewById(R.id.search_bar);
         lv = (ListView) findViewById(R.id.activity_list);
         googleMic = (ImageButton) findViewById(R.id.mic);

         Bundle b = getIntent().getExtras();
if(b!=null&&b.getString("phonenumber")!=null) {
    phoneNumber = b.getString("phonenumber");
}
         initList();

         googleMic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
startRecognizing();
             }
         });


         e.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 initList();
                     searchItem(s.toString());

             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });
     }

     private void startRecognizing(){
         Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                 RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
         intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                 "Speak your message...");
         startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
     }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
             ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
             voiceText = matches.get(0);

             Log.d("Result", voiceText);


             //  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
             if (activityList.contains(voiceText)) {
                 getTheTOken(voiceText);
             }
             else{
                  Toast.makeText(this, "Try again!",
                    Toast.LENGTH_LONG).show();
             }


         }
     }


     private void initList(){
         activityList = new ArrayList<>();
         activityList.addAll(Arrays.asList(getResources().getStringArray(R.array.activity_array)));
         adapter = new ArrayAdapter<String>(MySearchActivity.this, android.R.layout.simple_list_item_1, activityList);
         lv.setAdapter(adapter);
         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 getTheTOken(activityList.get(position).toString());

             }
         });
     }

     private String getTheTOken(final String activitySelected){

         reference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users").child(phoneNumber).child("token");
         reference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 replyToPing(activitySelected, dataSnapshot.getValue().toString());

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

         return "";
     }


     private void replyToPing(final String activitySelected, final String token){
         new Thread(){
             public void run(){
                 JSONObject jPayload = new JSONObject();
                 Log.d("here", token);
                 JSONObject jNotification = new JSONObject();
                 JSONObject jData=new JSONObject();
                 try {
                     jPayload.put("to",token);
                     jData.put("phonenumber", UserInformationActivity.phoneNumber);
                     jData.put("ping", "openreply");
                     jNotification.put("title", "PING REPLY");
                     jNotification.put("body", activitySelected);
                     jNotification.put("sound", "default");
                     jNotification.put("badge", "1");
                     jNotification.put("click_action", "PingHomeScreenActivity");

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
                     Toast.makeText(MySearchActivity.this,"Ping unsuccesfully",Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                 }

             }
         }.start();
     }
     private String convertStreamToString(InputStream is) {
         Scanner s = new Scanner(is).useDelimiter("\\A");
         return s.hasNext() ? s.next().replace(",", ",\n") : "";
     }




     private void searchItem(String item){
         for(Iterator<String>iterator=activityList.iterator();iterator.hasNext();){
             {
                 String value = iterator.next();
                 if(!value.contains(item)){
                      iterator.remove();
                 }
             }
         }
         adapter.notifyDataSetChanged();
     }
 }




