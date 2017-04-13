package edu.neu.madcourse.raj__kukadia.ping;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.raj__kukadia.R;

public class MyContactsActivity extends Activity  {

    private ListView listViewContacts;
    private Adapter contactsAdapter;
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
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
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

                ContentResolver cr=getContentResolver();

                Cursor cursor=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String [] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null,null,null);
                List<String> contacts=new ArrayList<String>();
                if((cursor.moveToNext())){
                    do{
                        contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+"  "+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

                    }while(cursor.moveToNext());
                }
        showMessage("Contacts",contacts.toString());

            }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

        }




