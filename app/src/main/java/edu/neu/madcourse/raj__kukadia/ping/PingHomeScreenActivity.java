package edu.neu.madcourse.raj__kukadia.ping;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment7.ScroggleMultiplayerAsyncFragment;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.PersistentModel;

import static edu.neu.madcourse.raj__kukadia.ping.MySearchActivity.VOICE_RECOGNITION_REQUEST_CODE;


public class PingHomeScreenActivity extends AppCompatActivity {

    private static final String FIRST_MSG = "Welcome to PING!";
    private static final String SECOND_MSG = "'Targets' are your ping friends, double tap to ping";
    private static final String THIRD_MSG = "'Received' has incoming messages, double tap to reply";
    private static final String NEW_MESSAGE = "New Message";
    private static final String GONE = "GONE";
    private static final String CONNECTIVITY_MESSAGE = "Internet connection lost!";
    private static final String CONNECTIVITY_MESSAGE_START = "No internet connection found!";
    private static final int VOICE_RECOGNITION_REQUEST_CODE_HOME = 11111;
    private static final String NOTIFICATION_MANAGER = "notification_manager";
    private static final String FOURTH_MSG = "It is best to provide a user name";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private Snackbar connectionSnackbar;
    private Snackbar connectionSnackbarStart;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionProctor connectionProctor;
    Handler mHandler;
    public static TextView userName;
    private ViewPager viewPager;
    private CircleImageView circleImageView;
    private ImageView imageView;
    private String voiceText;
    private SharedPreferences SP;
    private ArrayList<ContactUser> predictedUsers;
    private ArrayAdapter<String> predictedUsersAdapter;
    private SharedPreferences notificationManager;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_ping_home_screen);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
     //      <intent-filter>
       //         <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
         //       <action android:name="android.net.wifi.WIFI_STATE_CHANGED" />
           // </intent-filter>
       // IntentFilter intentFilter = new IntentFilter("edu.neu.madcourse.raj__kukadia.ping.android.net.conn.CONNECTIVITY_CHANGE");
        //intentFilter.addAction("edu.neu.madcourse.raj__kukadia.ping.android.net.wifi.WIFI_STATE_CHANGED");
        //registerReceiver(connectionProctor,intentFilter );
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Log.d("mSectionsPagerAdapter","");

        notificationManager = getSharedPreferences(NOTIFICATION_MANAGER, MODE_PRIVATE);

        userName = (TextView) findViewById(R.id.username);
      //  imageView = (ImageView) findViewById(R.id.imageView);
        circleImageView = (CircleImageView) findViewById(R.id.circleimageview);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_my_offers);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ping");


        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                userName = (TextView) findViewById(R.id.username);
                String username = SP.getString("username", "User Name");
                userName.setText(username);

                String previouslyEncodedImage = SP.getString("image_data", "");
                circleImageView = (CircleImageView) findViewById(R.id.circleimageview);

                if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                    byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    circleImageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                userName = (TextView) findViewById(R.id.username);
                String username = SP.getString("username", "User Name");
                userName.setText(username);

                String previouslyEncodedImage = SP.getString("image_data", "");
                circleImageView = (CircleImageView) findViewById(R.id.circleimageview);

                if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                    byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    circleImageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                //Toast.makeText(PingHomeScreenActivity.this, "pressed", Toast.LENGTH_LONG).show();

                if (id == R.id.nav_preferences) {
                    Intent intent = new Intent(PingHomeScreenActivity.this, MyPreferenceActivity.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_ack){
                    Intent intent=new Intent(PingHomeScreenActivity.this,AcknowledgementPing.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_about){
                    Intent intent=new Intent(PingHomeScreenActivity.this,About.class);
                    startActivity(intent);
                }
                if(id == R.id.nav_rateapp){
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" +getPackageName())));
                    }
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        viewPager = (ViewPager) findViewById(R.id.container);
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean welcome = SP.getBoolean("welcome", true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
if(welcome) {
    SP.edit().putBoolean("welcome", false).commit();
    final Snackbar snackbar  = Snackbar.make(coordinatorLayout, FIRST_MSG, Snackbar.LENGTH_INDEFINITE);
    View v1 = snackbar.getView();
    TextView t = (TextView) v1.findViewById(android.support.design.R.id.snackbar_text);
    t.setTextSize(18);
    snackbar.setAction("Thanks", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(snackbar.isShown()) {
                snackbar.dismiss();
            }
            final Snackbar snackbar1 = Snackbar.make(coordinatorLayout, SECOND_MSG, Snackbar.LENGTH_INDEFINITE);
            View v1 = snackbar1.getView();
            TextView t = (TextView) v1.findViewById(android.support.design.R.id.snackbar_text);
            t.setTextSize(17);
            snackbar1.setAction("Got it", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(snackbar1.isShown()){
                      snackbar1.dismiss();
                      final Snackbar snackbar2 = Snackbar.make(coordinatorLayout, THIRD_MSG, Snackbar.LENGTH_INDEFINITE);
                      View v1 = snackbar2.getView();
                      TextView t = (TextView) v1.findViewById(android.support.design.R.id.snackbar_text);
                      t.setTextSize(16);
                      snackbar2.setAction("Got it", new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              if(snackbar2.isShown()){
                                  snackbar2.dismiss();
                                  final Snackbar snackbar3 = Snackbar.make(coordinatorLayout, FOURTH_MSG, Snackbar.LENGTH_INDEFINITE);
                                  View v1 = snackbar3.getView();
                                  TextView t = (TextView) v1.findViewById(android.support.design.R.id.snackbar_text);
                                  t.setTextSize(16);
                                  snackbar3.setAction("Got it", new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if(snackbar3.isShown()){
                                              snackbar3.dismiss();
                                          }
                                      }
                                  });
                                  snackbar3.show();
                              }
                          }
                      });
                      snackbar2.show();
                  }
                }
            });
            snackbar1.show();
        }
    });
    snackbar.show();
        }
        ConnectivityManager c = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = c.getActiveNetworkInfo();//Active network info

        if (networkInfo != null && networkInfo.isConnected()) {

        }else{
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            connectionSnackbarStart = Snackbar.make(coordinatorLayout, CONNECTIVITY_MESSAGE_START, Snackbar.LENGTH_INDEFINITE);
            View v = connectionSnackbarStart.getView();
            TextView t = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            t.setTextColor(Color.RED);
            t.setTextSize(20);
            connectionSnackbarStart.show();        }
        predictedUsers = new ArrayList<>();

        predictedUsersAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

    }

public void notifyConnectionStatus(String status){
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

    if(status.equals(GONE)){
        connectionSnackbar = Snackbar.make(coordinatorLayout, CONNECTIVITY_MESSAGE, Snackbar.LENGTH_INDEFINITE);
        View v = connectionSnackbar.getView();
        TextView t = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
        t.setTextColor(Color.RED);
        t.setTextSize(20);
        connectionSnackbar.show();
    }else{
        if(connectionSnackbar!=null) if(connectionSnackbar.isShown()) connectionSnackbar.dismiss();

        if(connectionSnackbarStart!=null) if(connectionSnackbarStart.isShown()) connectionSnackbarStart.dismiss();
    }
}
public void TellForDoubleClick(){

}


public void notifyMessage(){
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    final Snackbar snackbar = Snackbar.make(coordinatorLayout, NEW_MESSAGE, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction("SHOW", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(snackbar.isShown()){
                viewPager = (ViewPager) findViewById(R.id.container);
                viewPager.setCurrentItem(1);
                snackbar.dismiss();
            }
        }
    });
    snackbar.show();
}

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ping_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.voice_ping) {
            SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            if(SP.getBoolean("firstvoice", true)) {
                coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

                Snackbar s = Snackbar.make(coordinatorLayout, "Speak the name in one word only", Snackbar.LENGTH_INDEFINITE).setAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                SP.edit().putBoolean("firstvoice", false).commit();
                    s.show();
            }
                startRecognizing();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startRecognizing(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak the name in one word...");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE_HOME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE_HOME && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceText = matches.get(0);
            boolean found = false;

            predictedUsers.clear();
            predictedUsersAdapter.clear();
            // Log.d("Result", voiceText);
            ArrayList<ContactUser> pingUsers = PersistentModel.getInstance().getPingUser();
            for(ContactUser c : pingUsers){
                String temp = c.getName().toLowerCase();
                for(String voice:matches) {
                    if (temp.contains(voice)) {
                        found = true;
                        predictedUsersAdapter.add(c.getName());

                     predictedUsers.add(c);
                        //   confirm(c, temp);
                       // break;
                    }
                }
                }

           // predictedUsers = new ArrayList<>();
if(found) {
    if(predictedUsers.size()==1){
        confirm(predictedUsers.get(0), predictedUsersAdapter.getItem(0));
    }
    else {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select what you meant");
        builder.setAdapter(predictedUsersAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                PersistentModel.getInstance().sendFCM(predictedUsers.get(item));

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}


            if(!found) Toast.makeText(this, "Try Again!",
                    Toast.LENGTH_LONG).show();

            //  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

        }

    }

    private void confirm(final ContactUser user, String bet){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        PersistentModel.getInstance().sendFCM(user);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you mean "+bet+" ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    public void updateOnPing(final ContactUser contactUser) {
        if(mHandler!=null)
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(0);
                if(fragment!=null)
                    if(fragment instanceof TargetsFragment)
                        contactUser.updateOnPinged();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public void updateReceiveListView() {
        if(mHandler!=null)
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(1);
                    if(fragment!=null)
                        if(fragment instanceof ReceivedFragment)
                            ((ReceivedFragment)fragment).contactFunction();           }
            });
    }

    public void onDoubleClickReceiveListener(ContactUser contactUser) {
        Intent intent=new Intent();
        intent.putExtra("phonenumber",contactUser.getNumber());
        startActivity(intent);

    }

    public void notifyTheUser(String Message) {
        final Snackbar snackbar  = Snackbar.make(coordinatorLayout, Message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        Fragment currentFragment;
        HashMap<Integer,Fragment>myHashMap=new HashMap<Integer,Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public HashMap<Integer, Fragment> getMyHashMap() {

            return myHashMap;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    TargetsFragment t = new TargetsFragment();
                    Log.d("Targetinscreen","old means");
                    myHashMap.put(position,t);
                    return t;
                case 1:
                    ReceivedFragment r = new ReceivedFragment();
                    myHashMap.put(position,r);
                    return r;
                case 2:
                    FriendsFragment f = new FriendsFragment();
                    myHashMap.put(position,f);
                    return f;

            }
            return null;
            //return null;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TARGETS";
                case 1:
                    return "RECEIVED";
                case 2:
                    return "FRIENDS";
            }
            return null;
        }

        public Fragment getCurrentFragment() {
            return currentFragment;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler=null;
        notificationManager.edit().putBoolean("online", false).commit();
        Log.d(String.valueOf(notificationManager.getBoolean("online", false)), "onnnPause");


    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler=new Handler(Looper.getMainLooper());

         notificationManager.edit().putBoolean("online", true).commit();
        Log.d(String.valueOf(notificationManager.getBoolean("online", false)), "onnnResume");



    }

    public void onClickViewListiner(final int position, final ContactUser contactUser){
        if(mHandler!=null)
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(2);
                if(fragment!=null)
                    if(fragment instanceof FriendsFragment)((FriendsFragment)fragment).onClickViewListiner(position,contactUser);            }
        });

    }


    public void updateTargetListView(){
        if(mHandler!=null)
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(0);
                    if(fragment!=null)
                        if(fragment instanceof TargetsFragment)
                            ((TargetsFragment)fragment).contactFunction();           }
            });

    }
}
