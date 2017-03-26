package edu.neu.madcourse.raj__kukadia.assignment7;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import edu.neu.madcourse.raj__kukadia.R;

public class ScoreBoardActivity extends Activity {

    private DatabaseReference mRootRef;
    private HashMap<String, String> scoreBoard = new HashMap<String, String>();
    private int i, j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_score_board);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Top Score Board");
        titleName.setTextSize(20);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        i =0;
        j=0;
        getScoreBoard();

    }



    private void getScoreBoard(){
DatabaseReference r = mRootRef.child("scoreBoard");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    scoreBoard.put(d.getKey().toString(), d.getValue().toString());
                }
       // HashMap<String, String> sortedScoreBoard=sort(scoreBoard);
                loadScoreBoard();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private HashMap<String, String> sort(HashMap scoreBoard){
        LinkedHashMap<String, String> sorted = new LinkedHashMap<String, String>();

Iterator s = scoreBoard.entrySet().iterator();
        for(int i =0; i<scoreBoard.size();i++) {
            while (s.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) s.next();

                if(s.next()!=null) {
                    HashMap.Entry nextPair = (HashMap.Entry) s.next();
                    int previousScore = Integer.parseInt(pair.getValue().toString());
                    String previousName = pair.getKey().toString();
                    int currentScore = Integer.parseInt(nextPair.getValue().toString());
                    String currentName = nextPair.getKey().toString();
                    if (currentScore > previousScore) {
                        sorted.put(currentName, String.valueOf(currentScore));
                        sorted.put(previousName, String.valueOf(previousScore));
                    } else {
                        sorted.put(previousName, String.valueOf(previousScore));
                        sorted.put(currentName, String.valueOf(currentScore));
                    }
                }else{
                    int currentScore = Integer.parseInt(pair.getValue().toString());
                    String currentName = pair.getKey().toString();
                    sorted.put(currentName, String.valueOf(currentScore));

                }
                s.remove();

            }
        }
return sorted;
    }

    private void loadScoreBoard(){

        LinearLayout list = (LinearLayout) findViewById(R.id.score_list);
        list.removeAllViews();

        Iterator s = scoreBoard.entrySet().iterator();
        while(s.hasNext()) {

            LinearLayout user = new LinearLayout(this);
            user.setOrientation(LinearLayout.HORIZONTAL);
            TextView name = new TextView(this);
            TextView score = new TextView(this);
            score.setTextSize(22);
            name.setTextSize(22);
            HashMap.Entry pair = (HashMap.Entry) s.next();
            name.setText(pair.getKey().toString()+"   ");
            score.setText("( "+pair.getValue().toString()+" )");
            user.addView(name);
            user.addView(score);
            list.addView(user);
            s.remove();

        }
    }


}