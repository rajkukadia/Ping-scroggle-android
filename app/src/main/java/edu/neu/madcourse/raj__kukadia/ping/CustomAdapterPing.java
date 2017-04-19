package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.raj__kukadia.R;

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

// Get detail element

        final ContactUser contactUser = (ContactUser) getItem(position);

// 2
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
                    ((MyContactsActivity)mContext).onClickViewListiner(position,contactUser);
                }
            });

// 3

        return rowView;
    }
}

