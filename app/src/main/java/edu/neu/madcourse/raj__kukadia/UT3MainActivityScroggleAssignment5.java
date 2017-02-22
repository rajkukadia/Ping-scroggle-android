package edu.neu.madcourse.raj__kukadia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class UT3MainActivityScroggleAssignment5 extends Activity {
  //  MediaPlayer mMediaPlayer;
    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setTitle("Word Game");

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main_scroggle_assignment5);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Word Game");

    }

    @Override
    protected void onResume() {
        super.onResume();
       // mMediaPlayer = MediaPlayer.create(this, R.raw.cyaronsgate);
        //mMediaPlayer.setVolume(0.5f, 0.5f);
        //mMediaPlayer.setLooping(true);
        //mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    mMediaPlayer.stop();
      //  mMediaPlayer.reset();
        //mMediaPlayer.release();
    }
}
