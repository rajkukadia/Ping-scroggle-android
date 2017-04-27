package edu.neu.madcourse.raj__kukadia.ping.persistent_model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.*;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;

/**
 * Created by Dharak on 4/19/2017.
 */

public class CustomAdapterAllActivity extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MyActivity> mDataSource;

    public CustomAdapterAllActivity(Context context, int resouce, ArrayList<MyActivity> items) {
        super(context,resouce,items);
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.layout_all_activity_ping, parent, false);
        TextView nameActivityTextView =
                (TextView) rowView.findViewById(R.id.allactivitiesTextView);
        TextView time=(TextView)rowView.findViewById(R.id.timeAllActivitiesTextView);
        MyActivity myActivity=mDataSource.get(position);

        if(myActivity.getActivityname()!=null){
            nameActivityTextView.setText(myActivity.getActivityname());
        }
        if(myActivity.getTimeString()!=null){
            time.setText(myActivity.getTimeString());
        }

        return rowView;
    }
}

