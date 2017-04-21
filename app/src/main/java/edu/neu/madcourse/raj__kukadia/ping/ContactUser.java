package edu.neu.madcourse.raj__kukadia.ping;

import android.support.annotation.NonNull;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Dharak on 4/12/2017.
 */

public class ContactUser implements Comparable<ContactUser>{
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

        DatabaseReference newReference= FirebaseDatabase.getInstance().getReference("Ping").child("All Users").child(number).child("token");
        newReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token=dataSnapshot.getValue(String.class);
                if(token!=null){
                    usesPing=true;
                    if(contactSearchButtonUpdate!=null){
                        if(contactSearchButtonUpdate.isShown())
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
}

