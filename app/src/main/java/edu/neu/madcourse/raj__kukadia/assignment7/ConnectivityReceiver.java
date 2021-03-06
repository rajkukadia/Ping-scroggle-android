package edu.neu.madcourse.raj__kukadia.assignment7;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

       if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)){

                 Toast.makeText(context, "Network Lost", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "Network Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}