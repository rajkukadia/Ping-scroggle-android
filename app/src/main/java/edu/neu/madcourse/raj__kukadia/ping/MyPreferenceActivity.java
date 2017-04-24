package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.neu.madcourse.raj__kukadia.R;

/**
 * Created by rajku on 4/23/2017.
 */

public class MyPreferenceActivity extends PreferenceActivity{

    private static final int GET_FROM_GALLERY = 99999;
    DatabaseReference mRootRef;
    Preference pref;
    SharedPreferences SP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_my_preference);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebarforping);

        TextView titleName = (TextView) findViewById(R.id.title_name_for_ping);
        titleName.setText("Preferences");
        titleName.setTextSize(20);
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mRootRef = FirebaseDatabase.getInstance().getReference("Ping");
        pref = findPreference("profilepic");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

                return false;
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                SharedPreferences.Editor edit=SP.edit();
                edit.putString("image_data",encodedImage);
                edit.commit();
                Toast.makeText(MyPreferenceActivity.this, "Profile picture changed", Toast.LENGTH_LONG).show();


            } catch (FileNotFoundException e) {
                Toast.makeText(MyPreferenceActivity.this, "File not found", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(MyPreferenceActivity.this, "Sorry, can't upload this file", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    private void notifyFireBase(String username){
        mRootRef.child("Ping Users").child(UserInformationActivity.phoneNumber).child("username").setValue(username);

    }


}
