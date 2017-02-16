package edu.neu.madcourse.raj__kukadia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {

   public static final HashMap<String, String> stringWords = new HashMap<String, String>();
    public static  final HashMap<Short, Short> threeWords = new HashMap<Short, Short>();
    public static final HashMap<Integer, Integer> fourWords = new HashMap<Integer, Integer>();
    public static final HashMap<Integer, Integer> fiveWords = new HashMap<Integer, Integer>();
    public static  final HashMap<Integer, Integer> sixWords = new HashMap<Integer, Integer>();
    // public static HashMap<Long, Long> seventotenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> sevenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> eightWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> nineWords = new HashMap<Long, Long>();
    public static final HashMap<Integer, Long> nineWordsCopy = new HashMap<Integer, Long>();

    public static final HashMap<Long, Long> tenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> elevenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> twelveWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> thirteenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> fourteenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> fifteenWords = new HashMap<Long, Long>();



    public static final HashMap<Long, Long> eleventofifteenWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> sixteentotwentyWords = new HashMap<Long, Long>();
    public static final HashMap<Long, Long> twentyonetotwentyfiveWords = new HashMap<Long, Long>();


    private int i = 1;
    public static boolean permission = true;
    long b = 0b00000;
    private static HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static HashMap <Character, Long> letterMap = new HashMap <Character, Long>();
    public static HashMap <Long, Character> reverseletterMap = new HashMap<Long, Character>();




    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
        getActionBar().setTitle("Raj Kukadia");
        setContentView(R.layout.activity_main);

        setHashMap();
        setMap();
        setListner(viewMap);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setMap(){
            for(char x= 'a';x<='z';x++){
                long binaryValue = getBinaryValue();
                letterMap.put(x,  binaryValue);
               reverseletterMap.put(binaryValue, x);
                }
        }


    private long getBinaryValue(){

            return ++b;

    }


    protected void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    permission = false;
                }

            }

        }
    }


    protected void setHashMap() {
        View v = findViewById(R.id.about_mainactivity_button);
        viewMap.put(1, v);
        v = findViewById(R.id.game_enter_button);
        viewMap.put(2, v);
        v = findViewById(R.id.generate_error_button);
        viewMap.put(3, v);
        v = findViewById(R.id.quit_button);
        viewMap.put(4, v);
        v = findViewById(R.id.dict_button);
        viewMap.put(5, v);

    }

    protected void setListner(HashMap view) {
        View v = (View) view.get(1);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        v = (View) view.get(2);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UT3MainActivity.class);
                startActivity(intent);
            }
        });
        v = (View) view.get(3);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 1 / 0;
            }
        });
        v = (View) view.get(4);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                System.exit(0);
            }
        });
        v = (View) view.get(5);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DictionaryAssignment3.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

