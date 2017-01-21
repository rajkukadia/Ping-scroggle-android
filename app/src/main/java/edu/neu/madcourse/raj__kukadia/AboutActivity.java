/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
 ***/
package edu.neu.madcourse.raj__kukadia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;

public class AboutActivity extends Activity implements View.OnClickListener {

    private AlertDialog mDialog;
    //private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static TelephonyManager mngr;
    private static String x;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("About me");
        setContentView(R.layout.activity_about);
        View v = findViewById(R.id.IMEI_button);
        builder = new AlertDialog.Builder(AboutActivity.this);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int permissionCheck = ContextCompat.checkSelfPermission(AboutActivity.this,
                //   Manifest.permission.READ_PHONE_STATE);


                //  try {
                //    Thread.sleep(1500);
                //} catch (InterruptedException e) {
                //  e.printStackTrace();
                //}



                mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                builder.setMessage( mngr.getDeviceId());
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }

                        });
                mDialog = builder.show();
                Log.d("error","s");

            }


        });



    }

   // protected void checkPermissions() {

//}




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

    }
}
