package edu.neu.madcourse.raj__kukadia;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {


    private static HashMap<Integer, View> viewMap = new HashMap<Integer, View>();

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Raj Kukadia");
        setContentView(R.layout.activity_main);
        //new MainActivity();
        setHashMap();
        setListner(viewMap);
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
                System.exit(0);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

