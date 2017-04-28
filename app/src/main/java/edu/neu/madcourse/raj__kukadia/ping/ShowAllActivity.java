package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.*;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.MyActivity;

/**
 * Created by Dharak on 4/26/2017.
 */

public class ShowAllActivity extends Activity {
    private String phoneNumber;
    private String userName;
    ArrayList<MyActivity> activityArrayList = new ArrayList<>();
    DatabaseReference reference ;
    DatabaseReference reference2;
    ListView activityListView;
    SwipeRefreshLayout swipe;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b=  getIntent().getExtras();

        phoneNumber = b.getString("phoneNumber");
        userName =b.getString("userName");


        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_showallactivity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebarforall);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        if (userName != null) {
            setTitle(userName + "'s Activities");
            titleName.setText(userName+"'s Activities");
        }
        titleName.setTextSize(20);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefreshAllActivity);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findActivities();
                swipe.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        findActivities();

    }

    private void findActivities() {
        reference= FirebaseDatabase.getInstance().getReference("Ping").child("Ping Users").
                child(String.valueOf(phoneNumber)).child("all activities");
        reference2= FirebaseDatabase.getInstance().getReference("Ping").child("Ping Users").
                child(String.valueOf(phoneNumber)).child("profilepic");

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String picture=dataSnapshot.getValue(String.class);
                if(picture!=null){
                    byte []picture2 = Base64.decode(picture,Base64.DEFAULT);
                    Bitmap finalPicture=BitmapFactory.decodeByteArray(picture2,0,picture2.length);
                    CircleImageView circleImageView=(CircleImageView) findViewById(R.id.displayPicture);
                    circleImageView.setImageBitmap(finalPicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // Log.d("phoneNumber=",phoneNumber);
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                activityArrayList=new ArrayList<MyActivity>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot1 != null) {
                        MyActivity myActivity = dataSnapshot1.getValue(MyActivity.class);
                        if (myActivity != null) {
                         activityArrayList.add(myActivity);
                        }
                    }

                }
                listViewInit();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

    private void listViewInit() {
        if(activityArrayList.size()<1){
            MyActivity myActivity=new MyActivity();
            myActivity.setActivityname("No activity");
            activityArrayList.add(myActivity);
        }
        else{

            Collections.sort(activityArrayList);
        }

        CustomAdapterAllActivity customAdapter=new CustomAdapterAllActivity(ShowAllActivity.this,android.R.layout.simple_list_item_1,activityArrayList);
        activityListView=(ListView) findViewById(R.id.all_activities_list);
        activityListView.setAdapter(customAdapter);
    }

}






