package edu.neu.madcourse.raj__kukadia.ping;

import android.Manifest;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.network.InternetThread;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

/**
 * Created by Dharak on 4/22/2017.
 */

public class ReceivedFragment extends Fragment {
    DatabaseReference reference;
    private ListView listViewContacts;
    ArrayList <ContactUser>pingUsers;
    View rootView;
    ListView listViewPingUsers;
    CustomAdapterTargetPing customAdapterTargetPing;
    ArrayList<ContactUser> duplicateListViewContacts;
    EditText searchBar;
    final int REQUEST_PERMISSION=123;
    private boolean permission;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(
                        R.layout.fragment_my_received_ping, container, false);
        //contactFunction();
        reference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users");
        this.rootView=rootView;
        //contactFunction();
        return rootView;
    }
    public void onResume() {
        super.onResume();
       // ((PingHomeScreenActivity)getActivity()).setFragment(this);
        onCreateHelper();
    }

    public void onCreateHelper() {
        listViewContacts = (ListView) rootView.findViewById(R.id.received_list);
        searchBar=(EditText)rootView. findViewById(R.id.received_search_bar);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {



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
                    permission = true;

                    contactFunction();
                    searchActivity();

                } else {
                    permission = false;
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
        listViewContacts=(ListView) rootView.findViewById(R.id.received_list);
        searchBar=(EditText) rootView.findViewById(R.id.received_search_bar);
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
        for(ContactUser contactUser:pingUsers) {
            duplicateListViewContacts.add(contactUser);
        }
        ArrayList<ContactUser>remove=new ArrayList();
        int i=0;
        for(ContactUser user:duplicateListViewContacts){
            Log.d(str,user.getName()+user.getName().toString().contains(str));
            if(!user.getName().toString().toLowerCase().contains(str.toLowerCase())){
                Log.d("Dharak",String.valueOf(i++));
                remove.add(user);
            }
        }
        for(ContactUser j:remove){
            duplicateListViewContacts.remove(j);
        }
        Log.d("removal=",Integer.toString(remove.size())+ duplicateListViewContacts.size());
        customAdapterTargetPing=new CustomAdapterTargetPing(getActivity(),android.R.layout.simple_list_item_1,duplicateListViewContacts);
        listViewContacts.setAdapter(customAdapterTargetPing);
        //contactsAdapter.notifyDataSetChanged();
    }

    public void contactFunction(){
        Log.d("ff","safd");
        PersistentModel.getInstance().loadContactFromPhone(getActivity());
        pingUsers=PersistentModel.getInstance().getReplyUser();
        duplicateListViewContacts=new ArrayList<>();
        for(ContactUser contactUser:pingUsers) {
            Log.d("got 1","Yipee");
            duplicateListViewContacts.add(contactUser);
        }
        customAdapterTargetPing=new CustomAdapterTargetPing(getActivity(),R.layout.layout_contact_ping,duplicateListViewContacts);
        listViewContacts.setAdapter(customAdapterTargetPing);

    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void onClickViewListiner(int position,ContactUser contactUser){
        if(contactUser.isUsesPing()){
            InternetThread.getinstance().addTasks(contactUser);
            Toast.makeText(getActivity(),"Pinged to "+contactUser.getName(),Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getActivity(),"SMS send to "+contactUser.getName(),Toast.LENGTH_SHORT).show();

        }
    }
}


