package edu.neu.madcourse.raj__kukadia.ping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.raj__kukadia.R;

/**
 * Created by rajku on 4/23/2017.
 */

public class MyPreferenceActivity extends PreferenceActivity{

    DatabaseReference mRootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mRootRef = FirebaseDatabase.getInstance().getReference("Ping");

        addPreferencesFromResource(R.xml.activity_my_preference);

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        notifyFireBase(sharedPreferences.getString("username", null));
                    }
                };

        SP.registerOnSharedPreferenceChangeListener(spChanged);

    }


    private void notifyFireBase(String username){
        mRootRef.child("Ping Users").child(UserInformationActivity.phoneNumber).child("username").setValue(username);

    }


}
