package edu.neu.madcourse.raj__kukadia;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {


    private static HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
        getActionBar().setTitle("Raj Kukadia");
        setContentView(R.layout.activity_main);
        //new MainActivity();
        setHashMap();
        setListner(viewMap);



    }

private void checkPermissions() {
    // Here, thisActivity is the current activity
    if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED)

    {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

    }

}
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected void setHashMap() {
        View v = findViewById(R.id.about_mainactivity_button);
        viewMap.put(1, v);
        v = findViewById(R.id.game_enter_button);
        viewMap.put(2, v);
        v = findViewById(R.id.generate_error_button);
        viewMap.put(3, v);
        v = findViewById(R.id.quit_button);
        viewMap.put(4, v);

    }

    protected void setListner(HashMap view) {
        View v = (View) view.get(1);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        v = (View) view.get(2);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UT3MainActivity.class);
                startActivity(intent);
            }
        });
        v = (View) view.get(3);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 1 / 0;
            }
        });
        v = (View) view.get(4);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

