package edu.neu.madcourse.raj__kukadia;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScroggleStatusAssignment5 extends Activity{

    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_status_assignment5);

        t1 = (TextView) findViewById(R.id.textview_scroggle_status_one);
        t2 = (TextView) findViewById(R.id.textview_scroggle_status_two);
        t3 = (TextView) findViewById(R.id.textview_scroggle_status_three);
        t4 = (TextView) findViewById(R.id.textview_scroggle_status_four);
        t5 = (TextView) findViewById(R.id.textview_scroggle_status_five);


        t1.setText("GAME OVER");
        t2.setText("Found Words");
        try {
            t3.setText(ScroggleAssignment5Fragment.e.getText().toString());
        }catch(NullPointerException e){

        }
            t4.setText("Total Score\n"+String.valueOf(ScroggleAssignment5Fragment.currentScore));
       t5.setText("Total Valid Clicks\n"+String.valueOf(ScroggleAssignment5Fragment.totalClicks));


    }
}