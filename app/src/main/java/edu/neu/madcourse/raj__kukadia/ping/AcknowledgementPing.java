package edu.neu.madcourse.raj__kukadia.ping;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import edu.neu.madcourse.raj__kukadia.R;

public class AcknowledgementPing extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_acknowledgement_ping);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebarforall);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("Acknowledgement");
        titleName.setTextSize(20);

    }
}