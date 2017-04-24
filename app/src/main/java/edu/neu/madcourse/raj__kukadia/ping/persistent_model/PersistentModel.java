package edu.neu.madcourse.raj__kukadia.ping.persistent_model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.raj__kukadia.ping.MySearchActivity;
import edu.neu.madcourse.raj__kukadia.ping.PingHomeScreenActivity;
import edu.neu.madcourse.raj__kukadia.ping.network.InternetThread;

/**
 * Created by Dharak on 4/21/2017.
 */

public class PersistentModel {
    private ArrayList<ContactUser> allContactUser ;
    private static final PersistentModel ourInstance = new PersistentModel();
    private ArrayList<ContactUser>pingUser;
    private ArrayList<ContactUser>receivedUser;
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
        for(ContactUser contactUser:getPingUser()){
            Log.d("contactUser","contactUser"+contactUser.getName()+contactUser.getNumber());
            if(contactUser.getNumber().equals(phoneNumer)) {
                Log.d("contactUser", "contactUser" + contactUser.getName() + contactUser.getNumber());
                return contactUser;
            }
        }
        for(ContactUser contactUser:getAllContactUser()){
            Log.d("contactUser","contactUser"+contactUser.getName()+contactUser.getNumber());
            if(contactUser.getNumber().equals(phoneNumer)) {
                Log.d("contactUser", "contactUser" + contactUser.getName() + contactUser.getNumber());
                return contactUser;
            }
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
    public ArrayList<ContactUser>getReplyUser(){
        if(allContactUser==null) return null;

        if(receivedUser==null) {
            receivedUser = new ArrayList<>();
            for (ContactUser contactUser : getAllContactUser()) {

                if (contactUser.isUsesPing()) receivedUser.add(contactUser);
            }
        }
        return receivedUser;
    }

    public void updateTargetFields(ContactUser contactUser){
        if(!pingUser.contains(contactUser)){
            if(!isdublicate(contactUser))
            pingUser.add(contactUser);
            else{
                allContactUser.remove(allContactUser.indexOf(contactUser));
            }
        }

        if(context!=null){
            ((PingHomeScreenActivity)context).updateTargetListView();
        }
    }
    public void updateReceiveFields(ContactUser contactUser){
        Log.d("adding","8573996996"+contactUser.getName());
        if(!receivedUser.contains(contactUser)) {
            receivedUser.add(contactUser);
        }
        if(context!=null){
          ((PingHomeScreenActivity)context).updateReceiveListView();
        }
    }

    public void sendReply(MySearchActivity object){
        InternetThread.getinstance().addTasks(object);
    }

    public void sendFCM(ContactUser contactUser){
        InternetThread.getinstance().addTasks(contactUser);
    }
    public void pingSuccessfull(ContactUser contactUser){
        if(context!=null)
            if(context instanceof PingHomeScreenActivity)
        ((PingHomeScreenActivity)context).updateOnPing(contactUser);
    }

    private boolean isdublicate(ContactUser contactUser){
        for(ContactUser newContactUser:pingUser){
            if(newContactUser.getName().equals(contactUser.getName()))
                if(newContactUser.getNumber().equals(contactUser.getNumber()))
                    return true;
        }
        return false;
    }


}
