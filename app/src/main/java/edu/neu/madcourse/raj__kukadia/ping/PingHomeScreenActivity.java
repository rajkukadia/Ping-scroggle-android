package edu.neu.madcourse.raj__kukadia.ping;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.ping.persistent_model.ContactUser;

public class PingHomeScreenActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Handler mHandler;
    public TextView userName;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_home_screen);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        userName = (TextView) findViewById(R.id.username);


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
            }

            @Override
            public void onDrawerClosed(View drawerView) {

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

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        mHandler=new Handler(Looper.getMainLooper());
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
        public void destroyItem (ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            myHashMap.remove(position);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
