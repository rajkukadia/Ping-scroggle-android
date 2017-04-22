package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;

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
                (TextView) rowView.findViewById(R.id.userNameContact);

        TextView time=(TextView)rowView.findViewById(R.id.timeTargetTextView);
        TextView Message=(TextView)rowView.findViewById(R.id.targetTextFieldMessage);
        //rowView.setPadding(100,100,100,100);

// Get detail element

        final ContactUser contactUser = getItem(position);
        userNameTextView.setText(contactUser.getName());
        if(contactUser.isPingRecent())
            time.setText(String.valueOf(contactUser.getPingTime()));
        Message.setText(contactUser.getMessageTargetScreen());

        userNameTextView.setText(contactUser.getName());


// 3

        return rowView;
    }
}

