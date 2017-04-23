package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
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
        userNameTextView.setText(contactUser.getName());
        if(contactUser.isPingRecent())
            time.setText(String.valueOf(contactUser.getPingTime()));
        Message.setText(contactUser.getMessageTargetScreen());
        userNameTextView.setText(contactUser.getName());
        contactUser.setMessageTargetFrament(Message);
        if(contactUser.getTargetScreenMessage()==ContactUser.TargetScreenMessage.LocallyPinged)
            Message.setTextColor(Color.RED);
        contactUser.setTimeMessage(time);
        if(contactUser.getTargetScreenMessage()== ContactUser.TargetScreenMessage.ShowActivity||contactUser.getTargetScreenMessage()== ContactUser.TargetScreenMessage.Pinged) {
            Long timefromContact = contactUser.getTime();
            if(timefromContact==null){
                time.setText(timefromContact.toString());
            }
        }
        contactUser.setTargetEntireViewGroup(rowView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactUser.getTargetScreenMessage()== ContactUser.TargetScreenMessage.InvalidStatus)
                PersistentModel.getInstance().sendFCM(contactUser);
            }
        });
        return rowView;
    }
}

