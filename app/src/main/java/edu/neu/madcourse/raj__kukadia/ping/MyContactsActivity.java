package edu.neu.madcourse.raj__kukadia.ping;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import edu.neu.madcourse.raj__kukadia.R;

public class MyContactsActivity extends Activity implements View.OnClickListener {

    private ListView myContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts_ping);

        myContacts = (ListView) findViewById(R.id.contact_list);


    }

    @Override
    public void onClick(View v) {

    }
}
