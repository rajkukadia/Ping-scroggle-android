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

public class CustomAdapterPing extends ArrayAdapter<ContactUser> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ContactUser> mDataSource;

    public CustomAdapterPing(Context context,int resouce,ArrayList<ContactUser> items) {
        super(context,resouce,items);
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.layout_contact_ping, parent, false);
        TextView userNameTextView =
                (TextView) rowView.findViewById(R.id.userNameContact);



        Button inviteButton=(Button)rowView.findViewById(R.id.isUsesPingFieldButton);
        //rowView.setPadding(100,100,100,100);

// Get detail element

        final ContactUser contactUser = getItem(position);

        userNameTextView.setText(contactUser.getName());
        contactUser.setButtonUpate(inviteButton);
        if(contactUser.isUsesPing()){
        inviteButton.setText("Ping");
            inviteButton.setVisibility(View.VISIBLE);
        }
        else{
            inviteButton.setVisibility(View.VISIBLE);
        }
            inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PingHomeScreenActivity)mContext).onClickViewListiner(position,contactUser);
                }
            });

// 3

        return rowView;
    }
}

