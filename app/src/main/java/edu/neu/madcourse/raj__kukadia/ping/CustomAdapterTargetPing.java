package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.network.InternetThread;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

/**
 * Created by Dharak on 4/19/2017.
 */

public class CustomAdapterTargetPing extends CustomAdapterPing {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ContactUser> mDataSource;

    public CustomAdapterTargetPing(Context context, int resouce, ArrayList<ContactUser> items) {
        super(context,resouce,items);
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.layout_target_ping, parent, false);
        TextView userNameTextView =
                (TextView) rowView.findViewById(R.id.userNameTarget);

        TextView time=(TextView)rowView.findViewById(R.id.timeTargetTextView);
        TextView Message=(TextView)rowView.findViewById(R.id.targetTextFieldMessage);
        final ContactUser contactUser = getItem(position);
        Log.d("contactUser",contactUser.toString());
        userNameTextView.setText(contactUser.getName());
        Message.setText(contactUser.getMessageTargetScreen());
        userNameTextView.setText(contactUser.getName());
        contactUser.setMessageTargetFrament(Message);
        if(contactUser.getTargetScreenMessage()==ContactUser.TargetScreenMessage.LocallyPinged)
            Message.setTextColor(Color.RED);
        contactUser.setTimeMessage(time);
        if(contactUser.getTargetScreenMessage()== ContactUser.TargetScreenMessage.ShowActivity||contactUser.getTargetScreenMessage()== ContactUser.TargetScreenMessage.Pinged) {
            String timefromContact = contactUser.getTime();
            if(timefromContact!=null){
                time.setText(timefromContact.toString());
                time.setVisibility(View.VISIBLE);
            }
        }
        contactUser.setTargetEntireViewGroup(rowView);

        rowView.setOnClickListener(new DoubleClickListenerView() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                if(contactUser.isActivityToRecent()){
                    //do something is Activity is too recent
                }
                else{
                   if(contactUser.isLocallyPing()) {
                       //do something if is locall pings

                   }else {
                       if (contactUser.isPingRecent()){
                           //show something if is recently pinged
                       }
                       else{
                           InternetThread.getinstance().addTasks(contactUser);
                       }
                   }
                }

            }
        });
        return rowView;
    }
}

