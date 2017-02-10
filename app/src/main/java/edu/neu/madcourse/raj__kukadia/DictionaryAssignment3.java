package edu.neu.madcourse.raj__kukadia;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DictionaryAssignment3 extends Activity {

    private static int x = 0;
    private char[] ans = new char[15];
    private static final String DEFAULT = "N/A";
    private char[] textInput = new char[15];
    private EditText mytext;
    private EditText result;
    private static boolean FirstSeven = true,Firsteight = true, Firstnine = true, Firstten = true,Firsteleven = true, Firsttwelve = true, Firstthirteen = true,Firstfourteen = true,Firstfifteen = true,FirstTimeCommingFor7to10 = true, FirstTimeCommingFor11to15 = true, FirstTimeCommingFor16to20 = true, FirstTimeCommingFor20to25 = true, FirstTimeCommingFor6to25 = true, FirstTimeCommingFor5and6 = true, FirstTimeCommingFor25Above = true;
    private char letter[];
    private long MyCharacter;

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
    InputStream is11;
    InputStream is12;
    InputStream is13;
    InputStream is14;
    InputStream is15;
    InputStream is16;
    InputStream is17;
    InputStream is18;



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
    DataInputStream din11;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Test Dictionary");
        setContentView(R.layout.activity_dictionary_ass3);
        createStreams();
        setthreewordfile();
        setfourwordfile();
        setButtonListeners();
        setDictListener();

    }

    private void createStreams() {
        is = getResources().openRawResource(R.raw.threewords);
        is1 = getResources().openRawResource(R.raw.fourwords);
        is2 = getResources().openRawResource(R.raw.fivewords);
        is3 = getResources().openRawResource(R.raw.sixwords);
        is4 = getResources().openRawResource(R.raw.seventotenwords);
        is5 = getResources().openRawResource(R.raw.eleventofifteenwords);
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
            din.close();
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

    private void setsevenfile(){
        for (int data = 0; data <= 47523; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seteightfile(){
        for (int data = 0; data <= 58447; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setninefile(){
        for (int data = 0; data <= 60121; data++) {
            Long d = null;
            try {
                d = din11.readLong();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.nineWords.put(d, d);

        }

        try {
            din11.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void settenfile(){
        for (int data = 0; data <= 55061; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setelevenfile(){
        for (int data = 0; data <= 45675; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void settwelvefile(){
        for (int data = 0; data <= 35470; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setthirteenfile(){
        for (int data = 0; data <= 25593; data++) {
            Long d = null;
            try {
                d = din15.readLong();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.thirteenWords.put(d, d);

        }

        try {
            din15.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setfourteenfile(){
        for (int data = 0; data <= 17591; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setfifteenfile(){
        for (int data = 0; data <= 11256; data++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







/*
    private void seteleventofifteenfile() {

        for (int data = 1; data <= 135585; data++) {
            Long d = null;
            try {
                d = din5.readLong();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.eleventofifteenWords.put(d, d);



        }

        try {
            din5.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

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


    public InputStream passobject() {
        return is4;
    }

    private void setDictListener() {

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
                        if (FirstTimeCommingFor16to20) {

                            setsixteentotwentyfile();
                            FirstTimeCommingFor16to20 = false;
                        }
                        MyCharacter = 0;

                        for (int l = 0; l < enteredString.length(); l++) {
                            MyCharacter = MyCharacter << 5;
                            MyCharacter += MainActivity.letterMap.get(letter[l]);
                        }


                        if (MyCharacter == MainActivity.sixteentotwentyWords.get(MyCharacter)) {
                            beep();
                            result.append(s.toString() + "\n");


                        }
                    } catch (NullPointerException e) {

                    }

                    MyCharacter = 0;
                } else if (enteredStringLength > 20 & enteredStringLength < 26) {

                    try {
                        if (FirstTimeCommingFor20to25) {

                            settwentyonetotwentyfivefile();
                            FirstTimeCommingFor20to25 = false;
                        }
                        MyCharacter = 0;

                        for (int l = 0; l < enteredString.length(); l++) {
                            MyCharacter = MyCharacter << 5;
                            MyCharacter += MainActivity.letterMap.get(letter[l]);
                        }


                        if (MyCharacter == MainActivity.twentyonetotwentyfiveWords.get(MyCharacter)) {
                            beep();
                            result.append(s.toString() + "\n");


                        }
                    } catch (NullPointerException e) {

                    }

                    MyCharacter = 0;


                } else if (enteredStringLength > 25) {
                    if (FirstTimeCommingFor25Above) {
                        set25abovewordfile();

                        FirstTimeCommingFor25Above = false;
                    }
                    if (enteredString.toString().equals(MainActivity.stringWords.get(enteredString.toString()))) {
                        beep();
                        result.append(enteredString.toString() + "\n");
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
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }


                                if ((short) MyCharacter == MainActivity.threeWords.get((short) MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                // Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            } catch (Exception e) {

                            }

                            MyCharacter = 0;
                            break;
                        }

                        case 4: {
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }


                                if ((int) MyCharacter == MainActivity.fourWords.get((int) MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();


                            }

                            MyCharacter = 0;
                            break;
                        }

                        case 5: {
                            if (FirstTimeCommingFor5and6) {
                                setfivewordfile();
                                setsixwordfile();
                                FirstTimeCommingFor5and6 = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ((int) MyCharacter == MainActivity.fiveWords.get((int) MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


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
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ((int) MyCharacter == MainActivity.sixWords.get((int) MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 7:
                        {
                            if (FirstSeven) {
                              setsevenfile();
                                FirstSeven = false;
                            }

                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter == MainActivity.sevenWords.get(MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 8:
                        {
                            if (Firsteight) {
                                seteightfile();
                                Firsteight = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.eightWords.get(MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 9:
                        {
                            if (Firstnine) {
                                setninefile();
                                Firstnine = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.nineWords.get( MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 10:
                        {
                            if (Firstten) {
                                settenfile();
                                Firstten = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.tenWords.get( MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 11:
                        {
                            if (Firsteleven) {
                                setelevenfile();
                                Firsteleven = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter == MainActivity.elevenWords.get( MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 12:
                        {
                            if (Firsttwelve) {
                                settwelvefile();
                                Firsttwelve = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.twelveWords.get( MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 13:
                        {
                            if (Firstthirteen) {
                                setthirteenfile();
                                Firstthirteen = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if (MyCharacter == MainActivity.thirteenWords.get(MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 14:
                        {
                            if (Firstfourteen) {
                                setfourteenfile();
                                Firstfourteen = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter == MainActivity.fourteenWords.get(MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

                            }

                            MyCharacter = 0;
                            break;
                        }
                        case 15:
                        {
                            if (Firstfifteen) {
                                setfifteenfile();
                                Firstfifteen = false;
                            }
                            try {
                                MyCharacter = 0;

                                for (int l = 0; l < enteredString.length(); l++) {
                                    MyCharacter = MyCharacter << 5;
                                    MyCharacter += MainActivity.letterMap.get(letter[l]);
                                }

                                if ( MyCharacter == MainActivity.fifteenWords.get( MyCharacter)) {
                                    beep();
                                    result.append(s.toString() + "\n");


                                }
                            } catch (NullPointerException e) {
                                //  Toast.makeText(DictionaryAssignment3.this, "illegal expression", Toast.LENGTH_LONG).show();

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
