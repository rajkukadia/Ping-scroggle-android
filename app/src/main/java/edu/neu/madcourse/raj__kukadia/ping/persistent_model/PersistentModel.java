package edu.neu.madcourse.raj__kukadia.ping.persistent_model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.raj__kukadia.ping.PingHomeScreenActivity;
import edu.neu.madcourse.raj__kukadia.ping.network.InternetThread;
import edu.neu.madcourse.raj__kukadia.ping.network.MyInternetQue;


/**
 * Created by Dharak on 4/21/2017.
 */

public class PersistentModel {
    private ArrayList<ContactUser> allContactUser ;
    private static final PersistentModel ourInstance = new PersistentModel();
    private ArrayList<ContactUser>pingUser;
    DatabaseReference reference;
    Context context;
    public static PersistentModel getInstance() {
        return ourInstance;
    }
    public ArrayList<ContactUser> getAllContactUser() {
        return allContactUser;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public void setAllContactUser(ArrayList<ContactUser> allContactUser) {
        this.allContactUser = allContactUser;
    }

    public static PersistentModel getOurInstance() {
        if(ourInstance!=null)return ourInstance;
        else{
           return new PersistentModel();
        }
    }

    private PersistentModel() {
        reference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users");

    }
    public void loadContactFromPhone(Context context){

        if(context instanceof PingHomeScreenActivity){
            this.context=context;
        }
        if(allContactUser==null){
            forceLoadContactFromPhone(context);
        }
        else{
            //do nothing because contact are already loaded
        }
    }
    public ContactUser getParticularUserByPhoneNumber(String phoneNumer){
        for(ContactUser contactUser:getAllContactUser()){
            if(contactUser.getNumber().equals(phoneNumer))return contactUser;
        }
        return null;
    }



    public void forceLoadContactFromPhone(Context context){
        if(context instanceof PingHomeScreenActivity)
        this.context=context;
        allContactUser=new ArrayList<>();
          ContentResolver cr=context.getContentResolver();

        Cursor cursor=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String [] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null,null,null);
        if((cursor.moveToNext())){
            do{
                String newContactNumber=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String newContactName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(newContactNumber.length()<10)continue;
                ContactUser newcontactUser=new ContactUser(newContactName,newContactNumber);
                allContactUser.add(newcontactUser);

            }while(cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(allContactUser);
    }


    public ArrayList<ContactUser>getPingUser(){
        if(allContactUser==null) return null;

        if(pingUser==null) {
            pingUser = new ArrayList<>();
            for (ContactUser contactUser : getAllContactUser()) {
                if (contactUser.isUsesPing()) pingUser.add(contactUser);
            }
        }
    return pingUser;
    }

    public void updateTargetFields(ContactUser contactUser){
        if(!pingUser.contains(contactUser)){
            pingUser.add(contactUser);
        }

        if(context!=null){
            ((PingHomeScreenActivity)context).updateTargetListView();
        }
    }

    public void sendFCM(ContactUser contactUser){
        InternetThread.getinstance().addTasks(contactUser);
    }
    public void pingSuccessfull(ContactUser contactUser){
        if(context!=null)
            if(context instanceof PingHomeScreenActivity)
        ((PingHomeScreenActivity)context).updateOnPing(contactUser);
    }


}
