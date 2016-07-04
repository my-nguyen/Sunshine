package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      if (savedInstanceState == null) {
         getSupportFragmentManager().beginTransaction()
               .add(R.id.container, new PlaceholderFragment())
               .commit();
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.detail, menu);
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
      }

      return super.onOptionsItemSelected(item);
   }

   /**
    * A placeholder fragment containing a simple view.
    */
   public class PlaceholderFragment extends Fragment {
      private String mForecast;

      public PlaceholderFragment() {
         setHasOptionsMenu(true);
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
         mForecast = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
         TextView textView = (TextView)rootView.findViewById(R.id.detail_text);
         textView.setText(mForecast);
         return rootView;
      }

      @Override
      public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         // inflate the menu; this adds items to the action bar if it's present
         inflater.inflate(R.menu.detail_fragment, menu);
         // retrieve the share menu item
         MenuItem item = menu.findItem(R.id.action_share);
         // Fetch reference to the share action provider
         ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
         // Attach an intent to this ShareActionProvider.  You can update this at any time, like when
         // the user selects a new piece of data they might like to share
         if (shareActionProvider != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mForecast + " #SunshineApp");
            shareActionProvider.setShareIntent(intent);
         } else
            Log.d("NGUYEN", "Share Action Provider is null");
      }
   }
}

