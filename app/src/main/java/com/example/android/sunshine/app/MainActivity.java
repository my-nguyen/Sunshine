package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.sunshine.app.data.DetailFragment;

public class MainActivity extends ActionBarActivity {
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";
    String mLocation;
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);
        if (findViewById(R.id.weather_detail_container) != null) {
            // the detail container will be present only in the large-screen layouts (res/layout-sw600dp).
            // if the view is present, then the activity should be in two-pane mode
            mTwoPane = true;
            // in two-pane mode, show the detail view in this activity by adding or replacing
            // the detail fragment using a fragment transaction
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                      .replace(R.id.weather_detail_container, new DetailFragment())
                      .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if (ff != null)
                ff.onLocationChanged();
            mLocation = location;
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_map) {
            // retrieve the preferred location from SharedPreferences
            String settingLocation = Utility.getPreferenceValue(this,
                  R.string.pref_location_key,
                  R.string.pref_location_default);
            Uri geoLocation = Uri.parse("geo:0,0?")
                  .buildUpon()
                  .appendQueryParameter("q", settingLocation)
                  .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
            if (intent.resolveActivity(getPackageManager()) == null)
                Toast.makeText(this, "Invalid geo location: " + settingLocation, Toast.LENGTH_SHORT).show();
            else
                startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
