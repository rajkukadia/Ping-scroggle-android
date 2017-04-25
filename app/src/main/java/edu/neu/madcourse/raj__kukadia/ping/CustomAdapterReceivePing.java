package edu.neu.madcourse.raj__kukadia.ping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

/**
 * Created by Dharak on 4/19/2017.
 */

public class CustomAdapterReceivePing extends CustomAdapterPing {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ContactUser> mDataSource;

    public CustomAdapterReceivePing(Context context, int resouce, ArrayList<ContactUser> items) {
        super(context,resouce,items);
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.layout_received_ping, parent, false);
        TextView userNameTextView =
                (TextView) rowView.findViewById(R.id.userNameReceived);

        TextView time=(TextView)rowView.findViewById(R.id.timeReceivedTextView);
        TextView Message=(TextView)rowView.findViewById(R.id.receivedTextFieldMessage);
        final ContactUser contactUser = getItem(position);
        userNameTextView.setText(contactUser.getName());
        time.setText(contactUser.getReceiveScreenTime());
        Message.setText(contactUser.getReceiveScreenMessage());
        rowView.setOnClickListener(new DoubleClickListenerView() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
            if(contactUser.getReceivedScreenMessage()== ContactUser.ReceivedScreenMessage.YetToReply)
            {
                Intent intent=new Intent(getContext(),MySearchActivity.class);
                intent.putExtra("phonenumber",contactUser.getNumber());
                (getContext()).startActivity(intent);

            }else{
                if(contactUser.getReceivedScreenMessage()== ContactUser.ReceivedScreenMessage.RepliedSuccessfully)
                PersistentModel.getInstance().notifyActivitySnacks("Replied already");

            }

            }
        });
        return rowView;
    }
}

