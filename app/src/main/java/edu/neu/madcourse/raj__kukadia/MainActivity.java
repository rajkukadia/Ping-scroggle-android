package edu.neu.madcourse.raj__kukadia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import edu.neu.madcourse.raj__kukadia.assignment5.UT3MainActivityScroggleAssignment5;
import edu.neu.madcourse.raj__kukadia.assignment7.GoogleSignInActivity;
import edu.neu.madcourse.raj__kukadia.ping.UserInformationActivity;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final HashMap<String, String> stringWords = new HashMap<String, String>();
    public static final HashMap<Short, Short> threeWords = new HashMap<Short, Short>();
    public static final HashMap<Integer, Integer> fourWords = new HashMap<Integer, Integer>();
    public static final HashMap<Integer, Integer> fiveWords = new HashMap<Integer, Integer>();
    public static final HashMap<Integer, Integer> sixWords = new HashMap<Integer, Integer>();
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
    private static HashMap<Integer, Button> viewMap = new HashMap<Integer, Button>();
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static HashMap<Character, Long> letterMap = new HashMap<Character, Long>();
    public static HashMap<Long, Character> reverseletterMap = new HashMap<Long, Character>();
    Thread loadTheDictionary;
    Handler handleLoadingDictionary;
    Thread runAnimation;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
        //getActionBar().setTitle("Raj Kukadia");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("Raj Kukadia");
        titleName.setTextSize(20);
        setHashMap();
        setMap();
        setListner(viewMap);

        loadTheDictionary = new Thread(new Dictionary());
        loadTheDictionary.start();

        handleLoadingDictionary = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("Here", "  it comes");
                //DictionaryAssignment3 instance = DictionaryAssignment3.getinstance();
                //instance.setDictListener();
                //d.onCreate(Bundle savedInstanceState);


            }
        };
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
     //   client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void showToast() {
        Toast.makeText(MainActivity.this, "Connection Lost", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setMap() {
        for (char x = 'a'; x <= 'z'; x++) {
            long binaryValue = getBinaryValue();
            letterMap.put(x, binaryValue);
            reverseletterMap.put(binaryValue, x);
        }
    }


    private long getBinaryValue() {

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
        Button v = (Button) findViewById(R.id.about_mainactivity_button);
        viewMap.put(1, v);
        v = (Button) findViewById(R.id.game_enter_button);
        viewMap.put(2, v);
        v = (Button) findViewById(R.id.generate_error_button);
        viewMap.put(3, v);
        v = (Button) findViewById(R.id.quit_button);
        viewMap.put(4, v);
        v = (Button) findViewById(R.id.dict_button);
        viewMap.put(5, v);
        v = (Button) findViewById(R.id.scroggle_button);
        viewMap.put(6, v);
        v = (Button) findViewById(R.id.communication_button);
        viewMap.put(7, v);
        v = (Button) findViewById(R.id.two_player_game_button);
        viewMap.put(8, v);
        v = (Button) findViewById(R.id.ping_button);
        viewMap.put(9, v);

    }

    protected void setListner(HashMap button) {
        Button v = (Button) button.get(1);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);

            }
        });

        v = (Button) button.get(2);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UT3MainActivity.class);
                startActivity(intent);


            }
        });
        v = (Button) button.get(3);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 1 / 0;
            }
        });
        v = (Button) button.get(4);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                System.exit(0);

                           }
        });
        v = (Button) button.get(5);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DictionaryAssignment3.class);

                startActivity(intent);

                           }
        });
        v = (Button) button.get(6);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread t = new Thread(new myThread());
                t.start();
                Intent intent = new Intent(MainActivity.this, UT3MainActivityScroggleAssignment5.class);
                startActivity(intent);


            }
        });

        v = (Button) button.get(7);
        v.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoogleSignInActivity.class));

            }
        }));
        v = (Button) button.get(8);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoogleSignInActivity.class));

            }
        });
        v = (Button) button.get(9);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInformationActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
/*
    public void didTapButton(View view) {
        Button button = (Button) findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
    }
*/
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

*/



    class Dictionary implements Runnable {
        InputStream is;
        InputStream is1;
        InputStream is2;
        InputStream is3;
        InputStream is4;
        InputStream is5;
        InputStream is6;
        InputStream is7;
        InputStream is8;
        InputStream is9;
        InputStream is10;
        //InputStream is11; Nine file taken out to mainactivity
        InputStream is12;
        InputStream is13;
        InputStream is14;
        InputStream is15;
        InputStream is16;
        InputStream is17;
        InputStream is18;
        InputStream is11;
        DataInputStream din11;

        DataInputStream din;
        DataInputStream din1;
        DataInputStream din2;
        DataInputStream din3;
        DataInputStream din4;
        DataInputStream din5;
        DataInputStream din6;
        DataInputStream din7;
        DataInputStream din8;
        DataInputStream din9;
        DataInputStream din10;
        //DataInputStream din11; Nine file taken oou to main activity
        DataInputStream din12;
        DataInputStream din13;
        DataInputStream din14;
        DataInputStream din15;
        DataInputStream din16;
        DataInputStream din17;
        DataInputStream din18;

        BufferedReader br;
        InputStreamReader inReader;

        @Override
        public void run() {
            createStreams();
            Message mg = Message.obtain();
            mg.arg1 = 0;

            // handleLoadingDictionary.sendMessage(mg);
            //  handleLoadingDictionary;


            setninefile();
            setthreewordfile();
            setfourwordfile();
            setfivewordfile();
            setsixwordfile();
            setsevenfile();
            seteightfile();
            settenfile();
            //setelevenfile();
            //settwelvefile();
            //setthirteenfile();
            //setfourteenfile();
            //setfifteenfile();
            //setsixteentotwentyfile();
            //settwentyonetotwentyfivefile();
            //set25abovewordfile();

        }


        private void setthreewordfile() {


            for (int data = 1; data <= 528; data++) {
                Long d = null;
                try {
                    d = din.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                short first = (short) (d >>> 45);
                long secondProgress = d << 19;
                short second = (short) (secondProgress >>> 49);
                long thirdProgress = d << 34;
                short third = (short) (thirdProgress >>> 49);
                long fourthProgress = d << 49;
                short fourth = (short) (fourthProgress >>> 49);
                MainActivity.threeWords.put(first, first);
                MainActivity.threeWords.put(second, second);
                MainActivity.threeWords.put(third, third);
                MainActivity.threeWords.put(fourth, fourth);

            }
            try {
                din.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        private void setfourwordfile() {

            for (int data = 1; data <= 2551; data++) {
                Long d = null;
                try {
                    d = din1.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int first = (int) (d >>> 40);
                long secondProgress = d << 24;
                int second = (int) (secondProgress >>> 44);
                long thirdProgress = d << 44;
                int third = (int) (thirdProgress >>> 44);
                MainActivity.fourWords.put(first, first);
                MainActivity.fourWords.put(second, second);
                MainActivity.fourWords.put(third, third);

            }
            try {
                din1.close();
                is1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        private void setfivewordfile() {

            for (int data = 1; data <= 8866; data++) {
                Long d = null;
                try {
                    d = din2.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int first = (int) (d >>> 25);
                long secondProgress = d << 39;
                int second = (int) (secondProgress >>> 39);

                MainActivity.fiveWords.put(first, first);
                MainActivity.fiveWords.put(second, second);

            }


            try {
                din2.close();
                is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        private void setsixwordfile() {

            for (int data = 1; data <= 16609; data++) {
                Long d = null;
                try {
                    d = din3.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int first = (int) (d >>> 30);
                long secondProgress = d << 34;
                int second = (int) (secondProgress >>> 34);
                MainActivity.sixWords.put(first, first);
                MainActivity.sixWords.put(second, second);

            }
            try {
                din3.close();
                is3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
    private void setseventotenfile() {
        for (int data = 1; data <= 221152; data++) {
            Long d = null;
            try {
                d = din4.readLong();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.seventotenWords.put(d, d);

        }

        try {
            din4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

        private void setsevenfile() {
            for (int data = 0; data < 47523; data++) {
                Long d = null;
                try {
                    d = din9.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.sevenWords.put(d, d);

            }

            try {
                din9.close();
                is9.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void seteightfile() {
            for (int data = 0; data < 58447; data++) {
                Long d = null;
                try {
                    d = din10.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.eightWords.put(d, d);

            }

            try {
                din10.close();
                is10.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void setninefile() {
            for (int data = 0; data < 60121; data++) {
                Long d = null;
                try {
                    d = din11.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.nineWords.put(d, d);
                MainActivity.nineWordsCopy.put(data, d);

            }

            try {
                din11.close();
                is11.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void settenfile() {
            for (int data = 0; data < 55061; data++) {
                Long d = null;
                try {
                    d = din12.readLong();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.tenWords.put(d, d);

            }

            try {
                din12.close();
                is12.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void setelevenfile() {
            for (int data = 0; data < 45675; data++) {
                Long d = null;
                try {
                    d = din13.readLong();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.elevenWords.put(d, d);

            }

            try {
                din13.close();
                is13.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void settwelvefile() {
            for (int data = 0; data < 35470; data++) {
                Long d = null;
                try {
                    d = din14.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.twelveWords.put(d, d);

            }

            try {
                din14.close();
                is14.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void setthirteenfile() {
            for (int data = 0; data < 25593; data++) {
                Long d = null;
                try {
                    d = din15.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    MainActivity.thirteenWords.put(d, d);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }

            try {
                din15.close();
                is15.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setfourteenfile() {
            for (int data = 0; data < 17591; data++) {
                Long d = null;
                try {
                    d = din16.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.fourteenWords.put(d, d);

            }

            try {
                din16.close();
                is16.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setfifteenfile() {
            for (int data = 0; data < 11256; data++) {
                Long d = null;
                try {
                    d = din17.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.fifteenWords.put(d, d);

            }

            try {
                din17.close();
                is17.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void setsixteentotwentyfile() {

            for (int data = 1; data <= 14447; data++) {
                Long d = null;
                try {
                    d = din6.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.sixteentotwentyWords.put(d, d);


            }

            try {
                din6.close();
                is6.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void settwentyonetotwentyfivefile() {

            for (int data = 1; data <= 416; data++) {
                Long d = null;
                try {
                    d = din7.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.twentyonetotwentyfiveWords.put(d, d);


            }

            try {
                din7.close();
                is7.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void set25abovewordfile() {
            String line;
            for (int data = 1; data <= 23; data++) {
                try {
                    if ((line = br.readLine()) != null) {
                        MainActivity.stringWords.put(line, line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void createStreams() {
            is = getResources().openRawResource(R.raw.threewords);
            is1 = getResources().openRawResource(R.raw.fourwords);
            is2 = getResources().openRawResource(R.raw.fivewords);
            is3 = getResources().openRawResource(R.raw.sixwords);
            //   is4 = getResources().openRawResource(R.raw.seventotenwords);
            //  is5 = getResources().openRawResource(R.raw.eleventofifteenwords);
            is6 = getResources().openRawResource(R.raw.sixteentotwentywords);
            is7 = getResources().openRawResource(R.raw.twentyonetotwentyfivewords);
            is8 = getResources().openRawResource(R.raw.twentyfiveabovewords);
            is9 = getResources().openRawResource(R.raw.sevenwords);
            is10 = getResources().openRawResource(R.raw.eightwords);
            is11 = getResources().openRawResource(R.raw.ninewords);
            is12 = getResources().openRawResource(R.raw.tenwords);
            is13 = getResources().openRawResource(R.raw.elevenwords);
            is14 = getResources().openRawResource(R.raw.twelvewords);
            is15 = getResources().openRawResource(R.raw.thirteenwords);
            is16 = getResources().openRawResource(R.raw.fourteenwords);
            is17 = getResources().openRawResource(R.raw.fifteenwords);


            inReader = new InputStreamReader(is8);
            br = new BufferedReader(inReader);

            din = new DataInputStream(is);
            din1 = new DataInputStream(is1);
            din2 = new DataInputStream(is2);
            din3 = new DataInputStream(is3);
            din4 = new DataInputStream(is4);
            din5 = new DataInputStream(is5);
            din6 = new DataInputStream(is6);
            din7 = new DataInputStream(is7);
            //din8 is not used
            din9 = new DataInputStream(is9);
            din10 = new DataInputStream(is10);
            din11 = new DataInputStream(is11);
            din12 = new DataInputStream(is12);
            din13 = new DataInputStream(is13);
            din14 = new DataInputStream(is14);
            din15 = new DataInputStream(is15);
            din16 = new DataInputStream(is16);
            din17 = new DataInputStream(is17);

        }


    }


    class myThread implements Runnable {


        @Override
        public void run() {
            Intent intent1 = new Intent(MainActivity.this, DictionaryAssignment3.class);
            intent1.putExtra("CallingActivity", MainActivity.class.toString());
            startActivity(intent1);
        }
    }


}

