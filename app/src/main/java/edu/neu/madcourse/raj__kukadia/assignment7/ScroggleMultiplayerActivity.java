package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.raj__kukadia.R;


public class ScroggleMultiplayerActivity extends Activity implements SensorListener {

    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public static MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private ScroggleMultiplayerFragment mGameFragment;
    private DatabaseReference mRootRef;
    private SensorManager sensorMgr;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 800;
    private float x, y,z =0;
    private float last_x, last_y,last_z = 0;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getActionBar().setTitle("Ultimate Tic Tac Toe");
        setContentView(R.layout.activity_scroggle_multiplayer);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mGameFragment = (ScroggleMultiplayerFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_multiplayer_scroggle);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener(ScroggleMultiplayerActivity.this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
        mAuth = FirebaseAuth.getInstance();

       // lastUpdate =System.currentTimeMillis()-200;
      //  boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        //if (restore) {
          //  String gameData = getPreferences(MODE_PRIVATE)
            //        .getString(PREF_RESTORE, null);
            //if (gameData != null) {
              //  mGameFragment.putState(gameData);
            //}
        //}
        //else{
          //  ScroggleMultiplayerFragment.currentScore=0;
        //}



     //   Log.d("UT3", "restore = " + restore);
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
        //String gameData = mGameFragment.getState();
      //  getPreferences(MODE_PRIVATE).edit()
        //        .putString(PREF_RESTORE, gameData)
          //      .commit();

      //  Log.d("UT3", "state = " + gameData);
     //   System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRootRef.child("SynchronousGames").child(ScroggleMultiplayerFragment.gameID).removeValue();
        finish();
    }


    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                if(y>4) {
                    mGameFragment.flipTiles(180);
                }
            else
                    if(x<-4){
                        mGameFragment.flipTiles(90);
            }
            else
                        if(y<-4){
                            mGameFragment.flipTiles(0);
                        }
            else
                        { mGameFragment.flipTiles(270);

                        }





            //System.out.println(mAuth.getCurrentUser().getDisplayName().toString()+" x: "+ x +"\ty: "+y);
//            Log.d(("X_Value"), String.valueOf(x));
//            Log.d(("Y_Value"), String.valueOf(y));
//            Log.d(("Z_Value"),String.valueOf(z));
        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }
}
