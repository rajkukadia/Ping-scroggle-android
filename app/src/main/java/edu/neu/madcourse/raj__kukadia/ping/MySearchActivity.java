 package edu.neu.madcourse.raj__kukadia.ping;

 import android.app.Activity;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Looper;
 import android.provider.ContactsContract;
 import android.speech.RecognizerIntent;
 import android.support.annotation.Nullable;
 import android.text.Editable;
 import android.text.TextWatcher;

 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.BaseAdapter;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.GridView;
 import android.widget.ImageButton;
 import android.widget.ImageView;
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
 import java.util.List;
 import java.util.Map;
 import java.util.Scanner;

 import edu.neu.madcourse.raj__kukadia.MainActivity;
 import edu.neu.madcourse.raj__kukadia.R;

 public class MySearchActivity extends Activity {

     private GridView gv;
     private GridView rgv;
     private EditText e;
     private ImageButton googleMic;
     private String voiceText;
     public static SharedPreferences recentActivities;
     public static final String ACTIVITY_STRING = "activity_string";
     private static final String RECENT_ACTIVITIES = "recent_activities";
     public static final String IMAGE_STRING = "image_string";
     // private ArrayAdapter<String> adapter;
     ArrayList<MyActivity> activityList;
     ArrayList<MyRecentActivity> recentActivityList;
     ArrayList<String> activityStringList;
     private String phoneNumber;
     Adapter adapter;
     RecentActivityAdapter recentAdapter;
             DatabaseReference reference;
     public static final int VOICE_RECOGNITION_REQUEST_CODE = 1230;

     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_my_search_ping);

         e = (EditText) findViewById(R.id.search_bar);

         recentActivities = getSharedPreferences(RECENT_ACTIVITIES, MODE_PRIVATE);

                googleMic = (ImageButton) findViewById(R.id.mic);
         Log.d("OnCreate", "called");

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
                 "Speak your activity...");
         startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
     }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
             ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
             voiceText = matches.get(0);

             Log.d("Result", voiceText);


             //  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
             if (activityStringList.contains(voiceText)) {
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
         recentActivityList = new ArrayList<>();
         activityStringList = new ArrayList<>();

         //activityList.addAll(Arrays.asList(getResources().getStringArray(R.array.activity_array)));
         //adapter = new ArrayAdapter<String>(MySearchActivity.this, android.R.layout.simple_list_item_1, activityList);
        if(activityStringList!=null){
         activityStringList.clear();}
         if(activityList!=null) {
             activityList.clear();
         }
         gv = (GridView) findViewById(R.id.activity_list);
         rgv = (GridView) findViewById(R.id.recent_activity_list);
         adapter = new Adapter(this);
         activityList = adapter.getList();
         for(int i = 0; i<activityList.size();i++){
             String x = activityList.get(i).activityName;
             activityStringList.add(x);
         }
         gv.setAdapter(adapter);


         gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   ViewHolder vh = (ViewHolder) view.getTag();
                 MyActivity ma = (MyActivity) parent.getAdapter().getItem(position);
                 Log.d("name", ma.activityName);

             //    MyRecentActivity mra = (MyRecentActivity) parent.getAdapter().getItem(position);
                 String activities ="";
                 String images = "";
                Log.d("recent", "zero");
                if(recentActivities.getString(IMAGE_STRING, null)!=null){
                     Log.d("recent", "one");
                     images = recentActivities.getString(IMAGE_STRING, null);

                    String []imageArray =  images.split(",");
                    List<String> s = new ArrayList<String>();
                    s  = Arrays.asList(imageArray);

                    if(s.size()>3) {
                        s = s.subList(0, 3);
                        images="";
                        for (String x : s) {
                            images =images+x + ",";
                        }
                        Log.d("NEW IMAGES", images);
                    }}


                 if(recentActivities.getString(ACTIVITY_STRING, null)!=null){
                     Log.d("recent", "two");
                     activities = recentActivities.getString(ACTIVITY_STRING, null);
                     String []tempString =  activities.split(",");
                     List<String> s = new ArrayList<String>();
                     s  = Arrays.asList(tempString);

if(s.size()>3) {
    s = s.subList(0, 3);
activities="";
    for (String x : s) {
        activities =activities+x + ",";
    }
    Log.d("NEW ACTIVITIES", activities);
}}

                 if(!activities.equals("")){
                     Log.d("recent", "three");
                     recentActivities.edit().putString(ACTIVITY_STRING, ma.activityName+","+activities).commit();
                 }
                 else {
                     Log.d("recent", "four");
                     recentActivities.edit().putString(ACTIVITY_STRING, ma.activityName).commit();
                 }

                 if(!images.equals("")){
                     Log.d("recent", "five");
                     recentActivities.edit().putString(IMAGE_STRING, ma.imageId+","+images).commit();
                 }
                 else {
                     Log.d("recent", "six");
                     recentActivities.edit().putString(IMAGE_STRING, String.valueOf(ma.imageId)).commit();
                 }

                // getTheTOken(ma.activityName);
             }
         });


        if(recentActivities.getString(ACTIVITY_STRING, null)!=null){
            recentAdapter = new RecentActivityAdapter(MySearchActivity.this);
            rgv.setAdapter(recentAdapter);
       }
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

         for(Iterator<String>iterator=activityStringList.iterator();iterator.hasNext();){
             {
                 String value = iterator.next();
                 Log.d("value", value);
                 if(!value.contains(item)){
                     Log.d("insidevalue", value);
                     for(int i = 0; i<activityList.size();i++){
                         if(activityList.get(i).activityName.equals(value)){
                             activityList.remove(i);
                                break;
                         }
                     }
                     iterator.remove();
                 }
             }
         }
         adapter.notifyDataSetChanged();
     }
 }

 class MyActivity {
     String activityName;
     int imageId;
     MyActivity(String activityName, int imageId){
         Log.d("Activity", "called");
         this.activityName = activityName;
         this.imageId = imageId;
     }
 }
 class MyRecentActivity {
     String activityName;
     int imageId;

     MyRecentActivity(String activityName, int imageId){
         Log.d("Activity", "called");
         this.activityName = activityName;
         this.imageId = imageId;

     }
 }
 class ViewHolder{

     ImageView myActivity;

     ViewHolder(View v){
         myActivity = (ImageView) v.findViewById(R.id.activity_image);
     }
 }

 class RecentActivityViewHolder{

     ImageView myActivity;

     RecentActivityViewHolder(View v){
         myActivity = (ImageView) v.findViewById(R.id.activity_image);
     }

 }



 class RecentActivityAdapter extends BaseAdapter{

     public ArrayList<MyRecentActivity> activityList;
     Context c;


     RecentActivityAdapter(Context c){
         Log.d("RecentActivityAdapter", "called");
         this.c = c;
         activityList = new ArrayList<>() ;

         String [] tempActivityName = (MySearchActivity.recentActivities.getString(MySearchActivity.ACTIVITY_STRING, null)).split(",");
         String [] tempImageId = (MySearchActivity.recentActivities.getString(MySearchActivity.IMAGE_STRING, null).split(","));
         for(int i = 0; i<tempActivityName.length;i++){
             MyRecentActivity tempActivity = new MyRecentActivity(tempActivityName[i], Integer.parseInt(tempImageId[i]));
             activityList.add(tempActivity);
             Log.d(tempActivityName[i], "recent_activity");
         }
     }
     public ArrayList<MyRecentActivity> getList(){
         return activityList;
     }

     @Override
     public int getCount() {
         return activityList.size();
     }

     @Override
     public Object getItem(int position) {
         return activityList.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         Log.d("RecentView", "called");
         View row = convertView;
         RecentActivityViewHolder holder = null;
         if(row == null){
             LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
             row = inflater.inflate(R.layout.activity_list, parent, false);
             holder = new RecentActivityViewHolder(row);
             row.setTag(holder);
         }
         else{
             holder = (RecentActivityViewHolder) row.getTag();
         }
         MyRecentActivity temp = activityList.get(position);

         holder.myActivity.setImageResource(temp.imageId);
         return row;
     }
 }

 class Adapter extends BaseAdapter{

     public ArrayList<MyActivity> activityList;
     Context c;

     Adapter(Context c){
         Log.d("Adapter", "called");
         this.c = c;
         activityList = new ArrayList<MyActivity>() ;
         String [ ] tempActivityName =c.getResources().getStringArray(R.array.activity_array);
         int [] tempImageId = {R.drawable.running, R.drawable.walking, R.drawable.bathing, R.drawable.cooking, R.drawable.dancing, R.drawable.biking, R.drawable.sitting};
         for(int i = 0; i<7;i++){
             MyActivity tempActivity = new MyActivity(tempActivityName[i], tempImageId[i]);
             activityList.add(tempActivity);
         }
     }

    public ArrayList<MyActivity> getList(){
        return activityList;
    }

     @Override
     public int getCount() {

         return activityList.size();
     }

     @Override
     public Object getItem(int position) {
         return activityList.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }



     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         Log.d("View", "called");
         View row = convertView;
         ViewHolder holder = null;
         if(row == null){
             LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
             row = inflater.inflate(R.layout.activity_list, parent, false);
             holder = new ViewHolder(row);
             row.setTag(holder);
         }
         else{
             holder = (ViewHolder) row.getTag();
         }
         MyActivity temp = activityList.get(position);

         holder.myActivity.setImageResource(temp.imageId);
         return row;
     }
 }