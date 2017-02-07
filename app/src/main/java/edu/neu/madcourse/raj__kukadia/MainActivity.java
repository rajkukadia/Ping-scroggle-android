package edu.neu.madcourse.raj__kukadia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {

    private int threedigit, fourdigit,fivedigit,sixdigit,sevendigit,edigit,ndigit,tendigit,elevendigit,tewlvedigit,thirteendigit,fourteendigit,fifteendifit,abovefifteen = 0;
    char letter[];
    long MyCharacter=0;
    private static Boolean firstTime = true;
    private int i = 1;
    private EditText mytext;
    public static boolean permission = true;
    long b = 0b00000;
    private static HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static HashMap <Character, Long> letterMap = new HashMap <Character, Long>();
    public static HashMap<String, String> stringWords = new HashMap<String, String>();
    public static HashMap<Short, Short> threeWords = new HashMap<Short, Short>();
    public static HashMap<Integer, Integer> fourWords = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> fiveWords = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> sixWords = new HashMap<Integer, Integer>();
    public static HashMap<Long, Long> seventotenWords = new HashMap<Long, Long>();
    public static HashMap<Long, Long> eleventofifteenWords = new HashMap<Long, Long>();
    public static HashMap<Long, Long> sixteentotwentyWords = new HashMap<Long, Long>();
    public static HashMap<Long, Long> twentyonetotwentyfiveWords = new HashMap<Long, Long>();





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
       // if (firstTime) {
         //   firstTime = false;
           // loadDictionary();
        //}

        setListner(viewMap);
       // readfirstpart();
    }


  /*  private void readfirstpart(){

            for (int data = 1; data <=(371600/2); data++) {
                Long d = null;
                try {
                    d = din4.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                seventotwentyfiveWords.put(d, d);

                //Log.d("six above words", " loading..");

            }

            try {
                din4.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


*/
    @Override
    protected void onResume() {
        super.onResume();

    }



        private void setMap(){
            for(char x= 'a';x<='z';x++){
                letterMap.put(x,  getBinaryValue());
                //System.out.println(letterMap);
            }
        }

    private long getBinaryValue(){

        //System.out.println(b);
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

