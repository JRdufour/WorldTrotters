package ca.worldtrotter.stclair.worldtrotters;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.twitter.sdk.android.core.Twitter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TripListFragment.OnFragmentInteractionListener,
        TripFragment.OnFragmentInteractionListener,
        AboutUsFragment.OnFragmentInteractionListener,
        NewInstanceFragment.OnFragmentInteractionListener,
        CreateTripFragment.OnFragmentInteractionListener,
        AddTripDateFragment.OnFragmentInteractionListener,
        SplashFragment.OnFragmentInteractionListener,
        TwitterFragment.OnFragmentInteractionListener{

    private static FragmentManager fm;
    public static FloatingActionButton fab;
    public static GoogleApiClient googleClient = null;
    //Variable used for splash screen
    private static int SPLASH_TIME_OUT = 4000;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize twitter
        Twitter.initialize(this);

        fm = getSupportFragmentManager();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        googleClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();

        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.main_content, new SplashFragment());
        t.addToBackStack(null);
        t.commit();

        /*
        * @author Said
        * Handler created to launch the splash screen
        */
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FragmentTransaction t = fm.beginTransaction();
                t.setCustomAnimations(R.anim.slide_in_left_fragment_animation, R.anim.slide_out_right_fragment_animation,
                        R.anim.slide_in_right, R.anim.slide_out_left);
                t.replace(R.id.main_content, new AboutUsFragment());
                t.addToBackStack(null);
                t.commit();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(googleClient != null){
            googleClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if(googleClient!=null && googleClient.isConnected()){
            googleClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(fm.findFragmentByTag("recycler") != null){
                Log.d("FRAGEMENT TEST", "TRUE");
                fm.popBackStack("recycler", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                Log.d("FRAGEMENT TEST", "FALSE");
                super.onBackPressed();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MainActivity.this, AppPreferences.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction t = fm.beginTransaction();

        if (id == R.id.nav_camera) {
            t.replace(R.id.main_content, new AboutUsFragment());
            t.addToBackStack(null);
            t.commit();
        } else if (id == R.id.nav_gallery) {
            t.replace(R.id.main_content, new TripListFragment());
            t.addToBackStack(null);
            t.commit();

        } else if (id == R.id.nav_slideshow) {
            t.replace(R.id.main_content, new TwitterFragment());
            t.addToBackStack(null);
            t.commit();


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
