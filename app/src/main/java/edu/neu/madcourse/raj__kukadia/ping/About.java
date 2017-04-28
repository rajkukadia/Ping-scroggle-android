package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.CustomAdapterAllActivity;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.MyActivity;

/**
 * Created by Dharak on 4/26/2017.
 */

public class About extends Activity {
    private String phoneNumber;
    private String userName;
    ArrayList<MyActivity> activityArrayList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_about_ping);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebarforall);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("About");
        titleName.setTextSize(20);



    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}






