package edu.neu.madcourse.raj__kukadia.ping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

/**
 * Created by rajku on 4/23/2017.
 */

public class MyPreferenceActivity extends PreferenceActivity{

    private static final int GET_FROM_GALLERY = 99999;
    DatabaseReference mRootRef;
    Preference pref;
    Uri mCapturedImageURI;
    static SharedPreferences SP;
    CheckBoxPreference checkBox;
    File file;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_my_preference);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebarforping);

        TextView titleName = (TextView) findViewById(R.id.title_name_for_ping);
        titleName.setText("Settings");
        titleName.setTextSize(20);
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mRootRef = FirebaseDatabase.getInstance().getReference("Ping");
        pref = findPreference("profilepic");
        checkBox = (CheckBoxPreference) findPreference("notification_updates");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent chooser = getPickImageIntent(MyPreferenceActivity.this);
                startActivityForResult(chooser, GET_FROM_GALLERY);

                return false;
            }
        });

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        if(key.equals("username"))
                        notifyFireBase(sharedPreferences.getString("username", null));
                        if(key.equals("notification_updates")||key.equals("pref_key_profile_settings")) {
                            try {
                                if (checkBox.isChecked()) handleNotificationSettings();
                                if (!checkBox.isChecked()) removeService();
                            }
                            catch (Exception e){

                            }
                        }
                    }
                };
        SP.registerOnSharedPreferenceChangeListener(spChanged);
    }

    private void removeService() {
       // Toast.makeText(this, "Stopping", Toast.LENGTH_LONG).show();
        stopService(new Intent(MyPreferenceActivity.this, FirebaseBackgroundService.class));
    }

    private void handleNotificationSettings() {
       if(checkBox.isChecked()){
        confirm();}
        else{
           removeService();
       }
    }

    private void confirm() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startService(new Intent(MyPreferenceActivity.this, FirebaseBackgroundService.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        checkBox.setChecked(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This may consume more battery, do you still want to continue?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public Intent getPickImageIntent(Context context) {
        File imageStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES)
                , "PingFolder");

        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }

        file = new File(
                imageStorageDir + File.separator + "IMG_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");

        Intent chooserIntent = null;

        Log.d("FileStorage", file.toString());

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Choose source");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }


    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage;
            if(data==null) {
                selectedImage = Uri.fromFile(file);
            }
            else{
                selectedImage = data.getData();
            }

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
              //  byte [] de =  Base64.decode(encodedImage, Base64.DEFAULT);
               // bitmap = BitmapFactory.decodeByteArray(de, 0, de.length);
                SharedPreferences.Editor edit=SP.edit();
                edit.putString("image_data",encodedImage);
                edit.commit();
                mRootRef.child("Ping Users").child(UserInformationActivity.phoneNumber).child("profilepic").setValue(encodedImage);
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
        Toast.makeText(MyPreferenceActivity.this, "User name changed", Toast.LENGTH_LONG).show();

    }


}
