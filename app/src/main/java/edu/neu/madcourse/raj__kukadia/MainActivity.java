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
    public static HashMap<String, String> dictMap = new HashMap<String, String>();
    public static HashMap<Long, Long> threeWords = new HashMap<Long, Long>();
    public static HashMap<Integer, Integer> fourWords = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> fiveWords = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> sixWords = new HashMap<Integer, Integer>();
    public static HashMap<Long, Long> sixAboveWords = new HashMap<Long, Long>();

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
        try {


           BufferedInputStream b = new BufferedInputStream(getResources().openRawResource(R.raw.threewords));
           BufferedInputStream b1 = new BufferedInputStream(getResources().openRawResource(R.raw.fourwords));
           BufferedInputStream b2 = new BufferedInputStream(getResources().openRawResource(R.raw.fivewords));
           BufferedInputStream b3 = new BufferedInputStream(getResources().openRawResource(R.raw.sixwords));
           BufferedInputStream b4 = new BufferedInputStream(getResources().openRawResource(R.raw.sixabovewords));

           DataInputStream din = new DataInputStream(b);
           DataInputStream din1 = new DataInputStream(b1);
           DataInputStream din2 = new DataInputStream(b2);
           DataInputStream din3 = new DataInputStream(b3);
           DataInputStream din4 = new DataInputStream(b4);

           // letter =("rat").toCharArray();

          /*  int c=0;
            while(c<3) {
                //MyCharacter=0;
                MyCharacter = MyCharacter << 5;
                MyCharacter += MainActivity.letterMap.get(letter[c]);
                c++;
            }
*/
            for (int data = 1; data <= 528; data++) {
                Long d = din.readLong();
                long first =  (d >>> 45);
                long secondProgress = d << 19;
                long second = (secondProgress >>> 49);
                long thirdProgress = d << 34;
                long third = (thirdProgress >>> 49);
                long fourthProgress = d << 49;
                long fourth = (d >>> 49);


              //  if((first == MyCharacter )||( second == MyCharacter)||(third ==MyCharacter)|| fourth== MyCharacter){
                //    Log.d("it is", "therrrrrrrrrrrrrrr");
                  //  Log.d("heelo","as");
                  // System.exit(0);
                //}
                threeWords.put(first, first);
                threeWords.put(second, second);
                threeWords.put(third, third);
                threeWords.put(fourth, fourth);
                Log.d("three words", " loading..");

            }

            for (int data = 1; data <= 2551; data++) {
                Long d = din1.readLong();
                int first = (int) (d >>> 40);
                long secondProgress = d << 24;
                int second = (int) (secondProgress >>> 44);
                long thirdProgress = d << 44;
                int third = (int) (thirdProgress >>> 44);
                fourWords.put(first, first);
                fourWords.put(second, second);
                fourWords.put(third, third);

                Log.d("four words", " loading..");

            }


            for (int data = 1; data <= 8866; data++) {
                Long d = din2.readLong();
                int first = (int) (d >>> 25);
                long secondProgress = d << 39;
                int second = (int) (secondProgress >>> 39);

                fiveWords.put(first, first);
                fiveWords.put(second, second);

                Log.d("five words", " loading..");

            }

/*
            for (int data = 1; data <= 16609; data++) {
                Long d = din3.readLong();
                int first = (int) (d >> 30);
                long secondProgress = d << 34;
                int second = (int) (secondProgress >> 34);

                sixWords.put(first, first);
                sixWords.put(second, second);

                Log.d("six words", " loading..");

            }


            for (int data = 1; data <=371622; data++) {
                Long d = din4.readLong();

                sixAboveWords.put(d, d);


                Log.d("six above words", " loading..");

            }


*/











            Toast.makeText(this, "Dictionary loaded successfully", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Log.d("Error:", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Error:", "IO Exception");
            e.printStackTrace();
        }










    }
        /*InputStream is = getResources().openRawResource(R.raw.wordlist);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {

            while ((line = br.readLine()) != null) {


                dictMap.put(line, line);
            }

             } catch (IOException e) {

            e.printStackTrace();
        }
    }
*/
        private void setMap(){
            for(char x= 'a';x<='z';x++){
                letterMap.put(x,  getBinaryValue());
                System.out.println(letterMap);
            }
        }

    private long getBinaryValue(){

        System.out.println(b);
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

