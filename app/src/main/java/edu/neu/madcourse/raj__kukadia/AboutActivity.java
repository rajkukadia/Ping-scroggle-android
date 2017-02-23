package edu.neu.madcourse.raj__kukadia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity implements View.OnClickListener {

    private AlertDialog mDialog;
    private static TelephonyManager mngr;
    private static String x;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setTitle("About me");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_about);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("About");
       // titleName.setAllCaps(true);
        //titleName.setTextAppearance(0);
        titleName.setTextSize(20);
        View v = findViewById(R.id.IMEI_button);
        builder = new AlertDialog.Builder(AboutActivity.this);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.permission) {
                    mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    builder.setMessage(mngr.getDeviceId());
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // nothing
                                }

                            });
                    mDialog = builder.show();
                }
                else{
                    builder.setMessage("You did not give the permit");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // nothing
                                }

                            });
                    mDialog = builder.show();
                }


            }


        });
    }

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
