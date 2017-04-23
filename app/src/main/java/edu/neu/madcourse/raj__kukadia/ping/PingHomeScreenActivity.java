package edu.neu.madcourse.raj__kukadia.ping;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_my_offers);
        tabLayout.setupWithViewPager(mViewPager);



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

    public void onClickViewListiner(int position,ContactUser contactUser){
        Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(2);
        if(fragment!=null)
        if(fragment instanceof FriendsFragment)((FriendsFragment)fragment).onClickViewListiner(position,contactUser);
    }

    public void updateTargetListView(){
        Fragment fragment=mSectionsPagerAdapter.getMyHashMap().get(0);
        if(fragment!=null)
            if(fragment instanceof TargetsFragment)
        ((TargetsFragment)fragment).contactFunction();
    }
}
