
package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleAssignment5Fragment;

public class ScroggleMultiplayerAsyncActivity extends Activity implements SensorListener {

    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public static MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private ScroggleMultiplayerAsyncFragment mGameFragment;
    private float x, y, z = 0;
    private SensorManager sensorMgr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_scroggle_multiplayer_async);



        mGameFragment = (ScroggleMultiplayerAsyncFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_multiplayer_scroggle_async);

        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener(ScroggleMultiplayerAsyncActivity.this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
        //  if (restore) {
        //    String gameData = getPreferences(MODE_PRIVATE)
        //          .getString(PREF_RESTORE, null);
        // if (gameData != null) {
        //   mGameFragment.putState(gameData);
        // }
        //}
        //else{
        //   ScroggleMultiplayerFragment.currentScore=0;
        //}


        Log.d("UT3", "restore = " + restore);

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
        //String gameData = mGameFragment.getState();
        //getPreferences(MODE_PRIVATE).edit()
        //      .putString(PREF_RESTORE, gameData)
        //    .commit();
        //Log.d("UT3", "state = " + gameData);

        if(mGameFragment.mDialog!=null){
        mGameFragment.mDialog.dismiss();}
        //mGameFragment.doTransactionForGameState();
        //   System.exit(0);
    }


    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {

            x = values[SensorManager.DATA_X];
            y = values[SensorManager.DATA_Y];
            z = values[SensorManager.DATA_Z];

            if (y > 4) {
                mGameFragment.flipTiles(180);
            } else if (x < -4) {
                mGameFragment.flipTiles(90);
            } else if (y < -4) {
                mGameFragment.flipTiles(0);
            } else {
                mGameFragment.flipTiles(270);
            }
        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }
}
