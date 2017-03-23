package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleAssignment5Fragment;
import edu.neu.madcourse.raj__kukadia.assignment5.TileAssignment5;

public class ScroggleMultiplayerActivity extends Activity {

    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public static MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private ScroggleMultiplayerFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getActionBar().setTitle("Ultimate Tic Tac Toe");
        setContentView(R.layout.activity_scroggle_multiplayer);
        mGameFragment = (ScroggleMultiplayerFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_multiplayer_scroggle);

        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                mGameFragment.putState(gameData);
            }
        }
        else{
            ScroggleMultiplayerFragment.currentScore=0;
        }



        Log.d("UT3", "restore = " + restore);
    }


    public void restartGame() {
      //  mGameFragment.restartGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = MediaPlayer.create(this, R.raw.syntheticity);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        Log.d("UT3", "state = " + gameData);
     //   System.exit(0);
    }



}
