 package edu.neu.madcourse.raj__kukadia.ping;

 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.Dialog;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.graphics.Color;
 import android.graphics.drawable.ColorDrawable;
 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Looper;
 import android.provider.ContactsContract;
 import android.speech.RecognizerIntent;
 import android.support.annotation.Nullable;
 import android.support.design.widget.CoordinatorLayout;
 import android.support.design.widget.Snackbar;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.Toolbar;
 import android.text.Editable;
 import android.text.TextWatcher;

 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.Window;
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
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Scanner;

 import edu.neu.madcourse.raj__kukadia.MainActivity;
 import edu.neu.madcourse.raj__kukadia.R;
 import edu.neu.madcourse.raj__kukadia.ping.applicatonlogic.myTasks;
 import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
 import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

 public class MySearchActivity extends AppCompatActivity implements myTasks {

     public static final String ACTIVITY_CONFIRM = "activity_confirm";
     public static final String ACTIVITY_CONFIRMED = "activity_confirmed";
     private static final String TEACH = "Always double tap to confirm";
     private GridView gv;
     private GridView rgv;
     private EditText e;
     private ImageButton googleMic;
     private String voiceText;
     private CoordinatorLayout coordinatorLayout;
     DatabaseReference mRootRef;
     private String currentSelectedActivity;
     private String token;
     private SharedPreferences activityConfirm;
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
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarreply);
         toolbar.setTitle("Ping");

         setSupportActionBar(toolbar);


         e = (EditText) findViewById(R.id.search_bar);

         activityConfirm = getSharedPreferences(ACTIVITY_CONFIRM, MODE_PRIVATE);

         recentActivities = getSharedPreferences(RECENT_ACTIVITIES, MODE_PRIVATE);

         coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutSearch);

        // activityConfirm.edit().remove(ACTIVITY_CONFIRMED).commit();

                googleMic = (ImageButton) findViewById(R.id.mic);
         Log.d("OnCreate", "called");

         Bundle b = getIntent().getExtras();
        if(b!=null&&b.getString("phonenumber")!=null) {
         phoneNumber = b.getString("phonenumber");
}
         initList();
        getTheTOken();
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

            boolean found =false;
             //  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

           for(String activityList: activityStringList) {
               for (String temp : matches) {
                   if (activityList.contains(temp)) {
                       confirm(temp);
                       found = true;

                   }
               }
           }
            if(!found) Toast.makeText(this, "Try again!",
                    Toast.LENGTH_LONG).show();

         }
     }

     private void confirm(final String bet){
         DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 switch (which){
                     case DialogInterface.BUTTON_POSITIVE:
                         //Yes button clicked
                         currentSelectedActivity = bet;
                         PersistentModel.getInstance().sendReply(MySearchActivity.this);
                         break;

                     case DialogInterface.BUTTON_NEGATIVE:
                         //No button clicked
                         break;
                 }
             }
         };

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("Do you mean "+bet+" ?").setPositiveButton("Yes", dialogClickListener)
                 .setNegativeButton("No", dialogClickListener).show();
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

         rgv.setOnItemClickListener(new DoubleClickListener() {
             @Override
             public void onSingleClick(View v, AdapterView<?> parent, int position) {
                 Boolean confirmation = activityConfirm.getBoolean(ACTIVITY_CONFIRMED, true);
                 if(confirmation){
                     teach();
                 }

             }

             @Override
             public void onDoubleClick(View v, AdapterView<?> parent, int position) {
                 MyRecentActivity mra = (MyRecentActivity) parent.getAdapter().getItem(position);
                 currentSelectedActivity = mra.activityName;
                 PersistentModel.getInstance().sendReply(MySearchActivity.this);
             }
         });


         gv.setOnItemClickListener(new DoubleClickListener() {
                                       @Override
                                       public void onSingleClick(View v, AdapterView<?> parent, int position) {
                                           Boolean confirmation = activityConfirm.getBoolean(ACTIVITY_CONFIRMED, true);
                                           if(confirmation){
                                               teach();
                                           }
                                       }

                                       @Override
                                       public void onDoubleClick(View view, AdapterView<?> parent, int position) {

                                           ViewHolder vh = (ViewHolder) view.getTag();
                                               MyActivity ma = (MyActivity) parent.getAdapter().getItem(position);
                                               Log.d("name", ma.activityName);

                                               //    MyRecentActivity mra = (MyRecentActivity) parent.getAdapter().getItem(position);
                                               String activities = "";
                                               String images = "";
                                               Log.d("recent", "zero");
                                               if (recentActivities.getString(IMAGE_STRING, null) != null) {
                                                   Log.d("recent", "one");
                                                   images = recentActivities.getString(IMAGE_STRING, null);

                                                   String[] imageArray = images.split(",");
                                                   List<String> s = new ArrayList<String>();
                                                   s = Arrays.asList(imageArray);

                                                   if (s.size() > 3) {
                                                       s = s.subList(0, 3);
                                                       images = "";
                                                       for (String x : s) {
                                                           images = images + x + ",";
                                                       }
                                                       Log.d("NEW IMAGES", images);
                                                   }
                                               }


                                               if (recentActivities.getString(ACTIVITY_STRING, null) != null) {
                                                   Log.d("recent", "two");
                                                   activities = recentActivities.getString(ACTIVITY_STRING, null);
                                                   String[] tempString = activities.split(",");
                                                   List<String> s = new ArrayList<String>();
                                                   s = Arrays.asList(tempString);
                                                   if (s.size() > 3) {
                                                       s = s.subList(0, 3);
                                                       activities = "";
                                                       for (String x : s) {
                                                           activities = activities + x + ",";
                                                       }
                                                       Log.d("NEW ACTIVITIES", activities);
                                                   }
                                               }

                                               if (!activities.equals("")) {
                                                   Log.d("recent", "three");
                                                   if (!activities.contains(ma.activityName)) {
                                                       recentActivities.edit().putString(ACTIVITY_STRING, ma.activityName + "," + activities).commit();
                                                   }
                                               } else {
                                                   Log.d("recent", "four");
                                                   recentActivities.edit().putString(ACTIVITY_STRING, ma.activityName).commit();
                                               }

                                               if (!images.equals("")) {
                                                   Log.d("recent", "five");
                                                   if (!images.contains(String.valueOf(ma.imageId))) {
                                                       recentActivities.edit().putString(IMAGE_STRING, ma.imageId + "," + images).commit();
                                                   }
                                               } else {
                                                   Log.d("recent", "six");
                                                   recentActivities.edit().putString(IMAGE_STRING, String.valueOf(ma.imageId)).commit();
                                               }

                                           currentSelectedActivity = ma.activityName;
                                           PersistentModel.getInstance().sendReply(MySearchActivity.this);
                                       }
                                   });



        if(recentActivities.getString(ACTIVITY_STRING, null)!=null){
            recentAdapter = new RecentActivityAdapter(MySearchActivity.this);
            rgv.setAdapter(recentAdapter);
       }
     }

     private void teach(){
         coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutSearch);
         Snackbar snackbar  = Snackbar.make(coordinatorLayout, TEACH, Snackbar.LENGTH_LONG);
         snackbar.show();
     }

     private void getTheTOken(){

         reference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users").child(phoneNumber).child("token");
         reference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot){
                token = dataSnapshot.getValue().toString();
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
             }
         });

     }


     private boolean run(){

                 JSONObject jPayload = new JSONObject();
                 Log.d("here", token);
                 JSONObject jNotification = new JSONObject();
                 JSONObject jData=new JSONObject();
                 try {
                     jPayload.put("to", token);
                     jData.put("phonenumber", UserInformationActivity.phoneNumber);
                     jData.put("ping", "openreply");
                     jNotification.put("title", "PING REPLY");
                     jNotification.put("body", currentSelectedActivity);
                     jNotification.put("sound", "default");
                     jNotification.put("badge", "1");
                     jNotification.put("click_action", "PingHomeScreensActivity");

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
                     if (resp.contains("\"success\":1")) {
                         return true;
                     }
                 } catch (JSONException | IOException e) {
                     Toast.makeText(MySearchActivity.this,"Ping unsuccesfully",Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                 }
return false;
     }

     private String convertStreamToString(InputStream is) {
         Scanner s = new Scanner(is).useDelimiter("\\A");
         return s.hasNext() ? s.next().replace(",", ",\n") : "";
     }

     private void searchItem(String item){

         for(Iterator<String>iterator=activityStringList.iterator();iterator.hasNext();){
             {
                 String value = iterator.next();
                 if(!value.contains(item)){
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


     private void putActivityToServer(){

         mRootRef = FirebaseDatabase.getInstance().getReference("Ping");
         Date date=new Date();

         long ts = date.getTime();
         edu.neu.madcourse.raj__kukadia.ping.Activity a = new edu.neu.madcourse.raj__kukadia.ping.Activity(currentSelectedActivity, ts);
         mRootRef.child("Ping Users").child(phoneNumber).child("activity").child("activityname").setValue(a.activityname);
         mRootRef.child("Ping Users").child(phoneNumber).child("activity").child("activitytimestamp").setValue(a.activitytimestamp);
     }

     @Override
     public boolean doTask() {
         putActivityToServer();
         return  run();
     }

     @Override
     public void OnTaskfailed() {

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
         int [] tempImageId = {R.drawable.running, R.drawable.walking, R.drawable.bathing, R.drawable.cooking, R.drawable.dancing, R.drawable.biking, R.drawable.sitting, R.drawable.badminton,
         R.drawable.soccer, R.drawable.guitar,R.drawable.baseball, R.drawable.gymming, R.drawable.bowling, R.drawable.skipping, R.drawable.treadmill,
         R.drawable.volleyball, R.drawable.music, R.drawable.brushing};
         for(int i = 0; i<tempImageId.length;i++){
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