package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.sunshine.app.data.DetailFragment;

public class MainActivity extends ActionBarActivity implements ForecastFragment.Callback {
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";
    String mLocation;
    boolean mTwoPane;

    @Override
    public void onItemSelected(Uri dateUri) {
        if (mTwoPane) {
            // on a tablet, show the detail view by adding or replacing the detail fragment
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                  .replace(R.id.weather_detail_container, fragment, DETAIL_FRAGMENT_TAG)
                  .commit();
        } else {
            // on a phone, launch DetailActivity
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setData(dateUri);
            startActivity(intent);
        }
    }

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
                      .replace(R.id.weather_detail_container, new DetailFragment(), DETAIL_FRAGMENT_TAG)
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
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            FragmentManager fm = getSupportFragmentManager();
            ForecastFragment ff = (ForecastFragment)fm.findFragmentById(R.id.fragment_forecast);
            if (null != ff) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)fm.findFragmentByTag(DETAIL_FRAGMENT_TAG);
            if (null != df) {
                df.onLocationChanged(location);
            }
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
