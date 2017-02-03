package edu.neu.madcourse.raj__kukadia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static android.R.attr.actionProviderClass;
import static android.R.attr.value;
import static android.widget.Toast.LENGTH_LONG;

public class DictionaryAssignment3 extends Activity {

    private static int x = 0;
    private char[] ans = new char[15];
    private static final String DEFAULT = "N/A";
    private char[] textInput = new char[15];
    private EditText mytext;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Dictionary");
        setContentView(R.layout.activity_dictionary_ass3);
        setDictListener();
        setButtonListeners();

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
                MainActivity.dictMap.get("a");

                if (s.toString().equalsIgnoreCase(MainActivity.dictMap.get(s.toString()))) {
                    beep();
                    result.append(s.toString() + "\n");

                }
            }
        });
    }

    private void beep(){
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                navigateUpTo(getParentActivityIntent());

            }
        });
    }
}
