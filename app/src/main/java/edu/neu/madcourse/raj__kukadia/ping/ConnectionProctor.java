package edu.neu.madcourse.raj__kukadia.ping;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

public class ConnectionProctor extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();



        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {


            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)){

              PersistentModel.getInstance().notifyForConnectivity("GONE");
              //   Toast.makeText(context, "Network Lost", Toast.LENGTH_LONG).show();

            } else {
                PersistentModel.getInstance().notifyForConnectivity("FETCHED");
                //Toast.makeText(context, "Network Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}