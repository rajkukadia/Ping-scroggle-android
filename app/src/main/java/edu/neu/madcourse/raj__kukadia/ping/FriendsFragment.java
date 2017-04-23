package edu.neu.madcourse.raj__kukadia.ping;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.Scanner;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.network.InternetThread;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

/**
 * Created by Dharak on 4/21/2017.
 */

public class FriendsFragment extends Fragment {
    private ArrayList<ContactUser> contactUserList=new ArrayList<>();
    private ListView listViewContacts;
    private ArrayList <ContactUser> duplicateListViewContacts=new ArrayList<>();
    private CustomAdapterPing contactsAdapter;
    DatabaseReference reference;
    EditText searchBar;
    private String token;
    final int REQUEST_PERMISSION=123;
    // EditText
    private boolean permission=false;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(
                        R.layout.fragment_my_contacts_ping, container, false);
        //contactFunction();
        reference=FirebaseDatabase.getInstance().getReference("Ping").child("All Users");
        this.rootView=rootView;
        //contactFunction();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        onCreateHelper();
    }

    public void onCreateHelper() {
        listViewContacts = (ListView) rootView.findViewById(R.id.contact_list);
        searchBar=(EditText)rootView. findViewById(R.id.contact_search_bar);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

            ActivityCompat.requestPermissions(getActivity(),
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
            searchActivity();//class addes serach activty
        }
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
                    searchActivity();

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


    private void searchActivity(){
        searchBar.setVisibility(View.VISIBLE);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                OntextChanged(searchBar.getText().toString());
            }
        });
    }
    private void OntextChanged(String str){
        duplicateListViewContacts=new ArrayList<>();
        for(ContactUser contactUser:contactUserList) {
            duplicateListViewContacts.add(contactUser);
        }
        ArrayList<ContactUser>remove=new ArrayList();
        int i=0;
        for(ContactUser user:duplicateListViewContacts){
            //Log.d(str,user.getName()+user.getName().toString().contains(str));
            if(!user.getName().toString().toLowerCase().contains(str.toLowerCase())){
                //Log.d("Dharak",String.valueOf(i++));
                remove.add(user);
            }
        }
        for(ContactUser j:remove){
            duplicateListViewContacts.remove(j);
        }
        //Log.d("removal=",Integer.toString(remove.size())+ duplicateListViewContacts.size());
        contactsAdapter=new CustomAdapterPing(getActivity(),android.R.layout.simple_list_item_1,duplicateListViewContacts);
        listViewContacts.setAdapter(contactsAdapter);
        //contactsAdapter.notifyDataSetChanged();
    }

    public void contactFunction(){
        /*
        if(!permission){
            showMessage("Permission Error","Permission to take contacts was denied");
            return;
        }
        */
        PersistentModel.getInstance().loadContactFromPhone(getActivity());
        contactUserList=PersistentModel.getInstance().getAllContactUser();
        duplicateListViewContacts=new ArrayList<>();
        for(ContactUser contactUser:contactUserList) {
            duplicateListViewContacts.add(contactUser);
        }
        contactsAdapter=new CustomAdapterPing(getActivity(),R.layout.layout_contact_ping,duplicateListViewContacts);
        listViewContacts.setAdapter(contactsAdapter);

    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void onClickViewListiner(int position,ContactUser contactUser){
        //Boolean result=findPlayerOnlineSend(contactUser.getNumber());
        if(contactUser.isUsesPing()){
            InternetThread.getinstance().addTasks(contactUser);
            Toast.makeText(getActivity(),"Pinged to "+contactUser.getName(),Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getActivity(),"SMS send to "+contactUser.getName(),Toast.LENGTH_SHORT).show();

        }
    }

}

