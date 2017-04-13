package edu.neu.madcourse.raj__kukadia.ping;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.raj__kukadia.R;

import static edu.neu.madcourse.raj__kukadia.ping.UserInformationActivity.SERVER_KEY;

public class MyContactsActivity extends Activity  {
    private ArrayList<ContactUser> contactUserList=new ArrayList<>();
    private ListView listViewContacts;
    private Adapter contactsAdapter;
    DatabaseReference reference;

    final int REQUEST_PERMISSION=123;
    private boolean permission=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts_ping);
        //contactFunction();
        listViewContacts = (ListView) findViewById(R.id.contact_list);

        if (ContextCompat.checkSelfPermission(MyContactsActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

                ActivityCompat.requestPermissions(MyContactsActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_PERMISSION);
                   // return;
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {
            permission = true;

            contactFunction();
        }
        //contactFunction();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission=true;

                    contactFunction();

                } else {
                    permission=false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void contactFunction(){
        /*
        if(!permission){
            showMessage("Permission Error","Permission to take contacts was denied");
            return;
        }
        */
        reference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users");
                //content Resolover
                ContentResolver cr=getContentResolver();

                Cursor cursor=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String [] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null,null,null);
                if((cursor.moveToNext())){
                    do{
                        String newContactNumber=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String newContactName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if(newContactName.length()<10)continue;
                        ContactUser newcontactUser=new ContactUser(newContactName,newContactNumber);
                        contactUserList.add(newcontactUser);

                    }while(cursor.moveToNext());
                }
                cursor.close();
        Collections.sort(contactUserList);
        String []listOfContacts=new String[contactUserList.size()];
        int i=0;
        for(ContactUser user:contactUserList){
            listOfContacts[i++]=user.getName();
        }

        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listOfContacts);
        listViewContacts.setAdapter(adapter);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //showMessage("Selected",contactUserList.get(position).toString());
                Boolean result=findPlayerOnlineSend(contactUserList.get(position).getNumber());
                if(result){
                    //showMessage("PING",contactUserList.get(position).getName()+ " is succesfully pinged");

                }else{
                    showMessage("INVITE",contactUserList.get(position).getName()+ " is not pinged please invite");

                }
            }
        });

 //       showMessage("Contacts",contactUserList.toString());

            }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public boolean findPlayerOnlineSend(String number){

        /*
            return true if player was found online and return true if succesfully  send an internet message
             otherwise returns false
         */
        DatabaseReference newReference=reference.child(number).child("token");
        if(newReference!=null){

            newReference.addListenerForSingleValueEvent(new ValueEventListener() {
                String token;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                            token = dataSnapshot.getValue().toString();
                    Log.d("tttttttttttt", token);

                    if(token!=null){
                        pushInternetFCM(token);
                    }
                    else{
                        return ;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }
        else{
            return false;
        }


    }
    public void pushInternetFCM(final String token){
        new Thread(){
         public void run(){
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
                         Toast.makeText(MyContactsActivity.this,"Ping succesfully",Toast.LENGTH_LONG).show();
                     }
                 });
             } catch (JSONException | IOException e) {
                 e.printStackTrace();
             }

         }
        }.start();
    }
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

        }




