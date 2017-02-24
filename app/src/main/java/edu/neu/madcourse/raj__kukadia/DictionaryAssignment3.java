package edu.neu.madcourse.raj__kukadia;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DictionaryAssignment3 extends Activity {

    private static int x = 0;
    private char[] ans = new char[15];
    private static final String DEFAULT = "N/A";
    private char[] textInput = new char[15];

    private boolean FirstSeven = true,Firsteight = true, Firstnine = true, Firstten = true,Firsteleven = true, Firsttwelve = true, Firstthirteen = true,Firstfourteen = true,Firstfifteen = true,FirstTimeCommingFor7to10 = true, FirstTimeCommingFor11to15 = true, FirstTimeCommingFor16to20 = true, FirstTimeCommingFor20to25 = true, FirstTimeCommingFor6to25 = true, FirstTimeCommingFor5and6 = true, FirstTimeCommingFor25Above = true;
    private   char letter[];
    private long MyCharacter;

    public static EditText mytext;
    public static EditText result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setTitle("Test Dictionary");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_dictionary_ass3);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("Dictionary");
        //titleName.setAllCaps(true);
        //titleName.setTextAppearance(0);
        titleName.setTextSize(20);
        TextView dictAnswer = (TextView) findViewById(R.id.anstext);
        dictAnswer.setEnabled(false);
        TextView dictSearch = (TextView) findViewById(R.id.searchtext);



        try {
            Bundle b = getIntent().getExtras();
            if (b.getString("CallingActivity").equals(MainActivity.class.toString())) {
                finish();
            }
        }
        catch(Exception e){

        }
        setButtonListeners();
        setDictListener();
    }

    public DictionaryAssignment3(){

    }


    private boolean checkExixtingContent(String s, EditText e){
        boolean response=true;

        String[] splitter  =e.getText().toString().split("\n");

        for (int split = 0; split<splitter.length;split++){
            // Log.d(splitter[split],"check");
            if(splitter[split].equalsIgnoreCase(s))
            {
                response=true;
                break;
            }
            else{
                response=false;
            }

        }

        return response;
    }


   // public InputStream passobject() {
       // return is4;
  //  }

    public void setDictListener() {
        result = (EditText) findViewById(R.id.anstext);
        mytext = (EditText) findViewById(R.id.searchtext);

        mytext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String enteredString = s.toString();
                int enteredStringLength = enteredString.length();
                letter = enteredString.toCharArray();

                if (enteredStringLength > 15 & enteredStringLength < 21) {

                    try {
                       // if (FirstTimeCommingFor16to20) {

                           // setsixteentotwentyfile();
                           // FirstTimeCommingFor16to20 = false;
                      //  }
                        MyCharacter = 0;

                        for (int l = 0; l < enteredString.length(); l++) {
                            MyCharacter = MyCharacter << 5;
                            if(letter[l]>=65&&letter[l]<=90){
                                letter[l]+=32;
                            }
                            MyCharacter += MainActivity.letterMap.get(letter[l]);
                        }


                        if (MyCharacter ==  MainActivity.sixteentotwentyWords.get(MyCharacter)) {
                            Boolean content = checkExixtingContent(s.toString(), result);
                            if (!content) {

                                beep();
                                result.append(s.toString() + "\n");
                            }

                        }
                    } catch (NullPointerException e) {

                    }

                    MyCharacter = 0;
                } else if (enteredStringLength > 20 & enteredStringLength < 26) {

                    try {
                      //  if (FirstTimeCommingFor20to25) {

                          //  settwentyonetotwentyfivefile();
                        //    FirstTimeCommingFor20to25 = false;
                      //  }
                        MyCharacter = 0;

                        for (int l = 0; l < enteredString.length(); l++) {
                            MyCharacter = MyCharacter << 5;
                            if(letter[l]>=65&&letter[l]<=90){
                                letter[l]+=32;
                            }
                            MyCharacter += MainActivity.letterMap.get(letter[l]);
                        }


                        if (MyCharacter ==  MainActivity.twentyonetotwentyfiveWords.get(MyCharacter)) {
                            Boolean content = checkExixtingContent(s.toString(), result);
                            if (!content) {

                                beep();
                                result.append(s.toString() + "\n");

                            }
                        }
                    } catch (NullPointerException e) {

                    }

                    MyCharacter = 0;


                } else if (enteredStringLength > 25) {
                   // if (FirstTimeCommingFor25Above) {
                    //    set25abovewordfile();

                   //     FirstTimeCommingFor25Above = false;
                  //  }
                    if (enteredString.toString().equals( MainActivity.stringWords.get(enteredString.toString()))) {
                        Boolean content = checkExixtingContent(s.toString(), result);
                        if (!content) {

                            beep();
                            result.append(enteredString.toString() + "\n");
                        }
                        }
                } else
                    switch (enteredStringLength) {
                        case 0:
                        case 1:
                        case 2:
                            break;
                        case 3: {
                            try {
                                MyCharacter = 0;


                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }


                                if ((short) MyCharacter ==  MainActivity.threeWords.get((short) MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);

                                    if(!content) {
                                        beep();
                                        result.append(s.toString() + "\n");
                                    }


                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                // Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();
                                Log.d("catches", " Exception");

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("catches", " Exception");
                            }

                            MyCharacter = 0;
                            break;
                        }

                        case 4: {
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }


                                if ((int) MyCharacter ==  MainActivity.fourWords.get((int) MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);

                                    if(!content) {
                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();


                            }

                            MyCharacter = 0;
                            break;
                        }

                        case 5: {
                          //  if (FirstTimeCommingFor5and6) {
                           //     setfivewordfile();
                            //    setsixwordfile();
                            //    FirstTimeCommingFor5and6 = false;
                          //  }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ((int) MyCharacter ==  MainActivity.fiveWords.get((int) MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if(!content) {
                                        beep();
                                        result.append(s.toString() + "\n");
                                    }


                                }
                            } catch (NullPointerException e) {
                                // Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }

                        case 6: {
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ((int) MyCharacter ==  MainActivity.sixWords.get((int) MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);

                                    if(!content) {
                                        beep();
                                        result.append(s.toString() + "\n");
                                    }


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 7:
                        {
                          //  if (FirstSeven) {
                           //   setsevenfile();
                            //    FirstSeven = false;
                           // }

                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter ==  MainActivity.sevenWords.get(MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 8:
                        {
                          //  if (Firsteight) {
                           //     seteightfile();
                           //     Firsteight = false;
                           // }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter ==  MainActivity.eightWords.get(MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 9:
                        {
                           // if (Firstnine) {
                             //   setninefile();
                               // Firstnine = false;
                            //}
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.nineWords.get( MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 10:
                        {
                           // if (Firstten) {
                            //    settenfile();
                            //    Firstten = false;
                           // }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter ==  MainActivity.tenWords.get( MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 11:
                        {
                            //if (Firsteleven) {
                            //    setelevenfile();
                            //    Firsteleven = false;
                           // }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter ==  MainActivity.elevenWords.get( MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 12:
                        {
                          //  if (Firsttwelve) {
                            //    settwelvefile();
                           //     Firsttwelve = false;
                           // }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter ==  MainActivity.twelveWords.get( MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");

                                    }
                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 13:
                        {
                           // if (Firstthirteen) {
                             //   setthirteenfile();
                             //   Firstthirteen = false;
                            //}
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter ==  MainActivity.thirteenWords.get(MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");
                                    }

                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 14:
                        {
                           // if (Firstfourteen) {
                            //    setfourteenfile();
                            //    Firstfourteen = false;
                           // }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter ==  MainActivity.fourteenWords.get(MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");

                                    }
                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 15:
                        {
                           // if (Firstfifteen) {
                            ///    setfifteenfile();
                            //    Firstfifteen = false;
                            //}
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    if(letter[l]>=65&&letter[l]<=90){
                                        letter[l]+=32;
                                    }
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter ==  MainActivity.fifteenWords.get( MyCharacter)) {
                                    Boolean content = checkExixtingContent(s.toString(), result);
                                    if (!content) {

                                        beep();
                                        result.append(s.toString() + "\n");

                                    }
                                }
                            } catch (NullPointerException e) {
                                //Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }

                        default:
                           Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();
                            break;
                    }
            }
        });
    }

    private void beep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 200);
    }

    private void setButtonListeners() {
        View clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                mytext.setText("");
            }
        });

        View returnButton = findViewById(R.id.button_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
              finish();
                //  navigateUpTo(getParentActivityIntent());

            }
        });

        View ackButton = findViewById(R.id.button_ack);
        ackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryAssignment3.this, AcknowledgementAssignment3.class);
                startActivity(intent);
            }
        });
    }
}
