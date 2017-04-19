package edu.neu.madcourse.raj__kukadia.ping;

import android.support.annotation.NonNull;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Comparator;

/**
 * Created by Dharak on 4/12/2017.
 */

public class ContactUser implements Comparable<ContactUser>{
    private String name;
    private String number;
    private String allowedNumber="1234567890";
    private boolean usesPing;
    private String token;
    private Button buttonUpate;
    public String getName() {
        return name;
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

    public Button getButtonUpate() {
        return buttonUpate;
    }

    public void setButtonUpate(Button buttonUpate) {
        this.buttonUpate = buttonUpate;
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
                    if(buttonUpate!=null){
                        buttonUpate.setText("Ping");
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
