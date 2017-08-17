package thosekids.com.finder_sohacks4;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationActivityMain extends AppCompatActivity {

    public Timer updateTimer = new Timer();


    static public String userTrackingId;
    static public Location target;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationmain);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        final long period = 3000;

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println();
                try {
                    sendLocation();
//                    updateTargetLocation(); // !!!
                }catch(Exception e) {}
            }
        }, 0, period);


    }


    public void sendLocation(){

        System.out.println("Updating Location");

        final Location loc = getLastKnownLocation();
        LocationUpdate pos = new LocationUpdate(loc.getLongitude(), loc.getLatitude());
        databaseReference.child("Locations").child(firebaseAuth.getCurrentUser().getUid()).setValue(pos);
        System.out.println("My location: (" + loc.getLatitude() + " , " + loc.getLongitude() + ")");




        try {
            System.out.println("Their location: (" + target.getLatitude() + " , " + target.getLongitude() + ")");
        } catch(Exception e) {}

    }

    public void updateTargetLocation() {

        // !!!
        databaseReference.child("Locations").child(userTrackingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                double longgitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                Location loc = new Location("");
                loc.setLongitude(longitude);
                loc.setLatitude(latitude);
                target = loc;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    LocationManager mLocationManager;
    Location location;
    double longitude;
    double latitude;

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            try {
                l = mLocationManager.getLastKnownLocation(provider);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
/*    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

    }*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0){
                fragment = new FragmentCompass();
            }
            if (position == 1){
                fragment = new FragmentMap();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Compass";
                case 1:
                    return "Map";
            }
            return null;
        }
    }
}
