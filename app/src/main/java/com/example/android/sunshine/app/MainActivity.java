package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
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
            Log.d("NGUYEN", "Location, setting: " + settingLocation + ", geo: " + geoLocation);
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
