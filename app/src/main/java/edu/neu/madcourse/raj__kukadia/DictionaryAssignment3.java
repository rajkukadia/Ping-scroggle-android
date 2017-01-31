package edu.neu.madcourse.raj__kukadia;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import static android.R.attr.value;
import static android.widget.Toast.LENGTH_LONG;

public class DictionaryAssignment3 extends Activity{

    private static int  x = 0;
    private char[] ans = new char[15];
    private static final String DEFAULT = "N/A";
    private char [] textInput = new char[15];
    private EditText mytext;
    private EditText result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Dictionary");
        setContentView(R.layout.activity_dictionary_ass3);

        setDictListener();

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


                  //   SharedPreferences sp = getSharedPreferences("MyDictionary", Context.MODE_PRIVATE);
                //HashMap<Double, String> dictMap = (HashMap<Double, String>) getIntent().getSerializableExtra("dictMap");

                    int i = 1;
               try {
                   switch (s.charAt(0)) {
                       case 'a':
                           while (MainActivity.dictMap.get(i).startsWith("a")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");

                               }
                               i++;
                           }
                           i = 1;
                           break;


                       case 'b':
                           while (MainActivity.dictMap.get(i).charAt(0)<='b') {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                           i=1;
                           break;


                       case 'c':
                           while (MainActivity.dictMap.get(i).charAt(0)<='c') {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }

                       break;
                       case 'd': {
                           while (MainActivity.dictMap.get(i).charAt(0)<='d') {
                               String value = MainActivity.dictMap.get(i);

                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'e': {
                           while (MainActivity.dictMap.get(i).charAt(0)<='e') {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'f': {
                           while (MainActivity.dictMap.get(i).startsWith("f")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'g': {
                           while (MainActivity.dictMap.get(i).startsWith("g")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'h': {
                           while (MainActivity.dictMap.get(i).startsWith("h")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'i': {
                           while (MainActivity.dictMap.get(i).startsWith("i")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'j': {
                           while (MainActivity.dictMap.get(i).startsWith("j")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'k': {
                           while (MainActivity.dictMap.get(i).startsWith("k")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'l': {
                           while (MainActivity.dictMap.get(i).startsWith("l")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'm': {
                           while (MainActivity.dictMap.get(i).startsWith("m")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'n': {
                           while (MainActivity.dictMap.get(i).startsWith("n")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'o': {
                           while (MainActivity.dictMap.get(i).startsWith("o")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'p': {
                           while (MainActivity.dictMap.get(i).startsWith("p")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'q': {
                           while (MainActivity.dictMap.get(i).startsWith("q")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'r': {
                           while (MainActivity.dictMap.get(i).startsWith("r")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 's': {
                           while (MainActivity.dictMap.get(i).startsWith("s")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 't': {
                           while (MainActivity.dictMap.get(i).startsWith("t")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'u': {
                           while (MainActivity.dictMap.get(i).startsWith("u")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'v': {
                           while (MainActivity.dictMap.get(i).startsWith("v")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'w': {
                           while (MainActivity.dictMap.get(i).startsWith("w")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'x': {
                           while (MainActivity.dictMap.get(i).startsWith("x")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'y': {
                           while (MainActivity.dictMap.get(i).startsWith("y")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       case 'z': {
                           while (MainActivity.dictMap.get(i).startsWith("z")) {
                               String value = MainActivity.dictMap.get(i);
                               if (s.toString().equalsIgnoreCase(value)) {
                                   result.append(value + " ");
                               }
                               i++;
                           }
                       }
                       break;
                       default:
                           Toast.makeText(DictionaryAssignment3.this, "No such data", LENGTH_LONG).show();


                   }
               }catch(Exception e){
                   Toast.makeText(DictionaryAssignment3.this, "Cleared", LENGTH_LONG).show();
               }
                    }
                    //Log.d("hello ", value);

                //x++;




        });


    }


}
