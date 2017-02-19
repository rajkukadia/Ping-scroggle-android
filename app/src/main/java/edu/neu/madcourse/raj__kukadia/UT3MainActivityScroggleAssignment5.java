package edu.neu.madcourse.raj__kukadia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class UT3MainActivityScroggleAssignment5 extends Activity {
    MediaPlayer mMediaPlayer;
    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Word Game");
        setContentView(R.layout.activity_main_scroggle_assignment5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = MediaPlayer.create(this, R.raw.cyaronsgate);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }
}
