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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {

    private static Boolean firstTime = true;
    private int i = 1;
    private EditText mytext;
    public static boolean permission = true;
    private static HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static HashMap<String, String> dictMap = new HashMap<String, String>();

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
        getActionBar().setTitle("Raj Kukadia");
        setContentView(R.layout.activity_main);
        setHashMap();

        if (firstTime) {
            firstTime = false;
            loadDictionary();
        }

        setListner(viewMap);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void loadDictionary() {
        InputStream is = getResources().openRawResource(R.raw.wordlist);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {

            while ((line = br.readLine()) != null) {
                dictMap.put(line, line);
            }
            Boolean ans = dictMap.containsKey("ahh");
            Log.d("as is", String.valueOf(ans));
            Toast.makeText(this, "Dictionary loaded successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {

            e.printStackTrace();
        }
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

