package com.example.android.sunshine.app.data;

/**
 * Created by My on 7/12/2016.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;

// A placeholder fragment containing a simple view.
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
   private static final String LOG_TAG = DetailFragment.class.getSimpleName();
   public static final String DETAIL_URI = "URI";
   private static final int DETAIL_LOADER = 0;
   private static final String[] DETAIL_COLUMNS = {
         WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
         WeatherContract.WeatherEntry.COLUMN_DATE,
         WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
         WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
         WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
         WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
         WeatherContract.WeatherEntry.COLUMN_PRESSURE,
         WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
         WeatherContract.WeatherEntry.COLUMN_DEGREES,
         WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
   };
   // these constants correspond to the projection defined above, and must change if the
   // projection changes
   private static final int COL_WEATHER_ID = 0;
   private static final int COL_WEATHER_DATE = 1;
   private static final int COL_WEATHER_DESC = 2;
   private static final int COL_WEATHER_MAX_TEMP = 3;
   private static final int COL_WEATHER_MIN_TEMP = 4;
   private static final int COL_WEATHER_HUMIDITY = 5;
   private static final int COL_WEATHER_PRESSURE = 6;
   private static final int COL_WEATHER_WIND_SPEED = 7;
   private static final int COL_WEATHER_DEGREES = 8;
   private static final int COL_WEATHER_CONDITION_ID = 9;

   private ShareActionProvider mShareActionProvider;
   private String mForecastText;
   private TextView mDay;
   private TextView mDate;
   private TextView mHigh;
   private TextView mLow;
   private ImageView mImage;
   private TextView mForecast;
   private TextView mHumidity;
   private TextView mWind;
   private TextView mPressure;
   private Uri mUri;

   public DetailFragment() {
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Bundle arguments = getArguments();
      if (arguments != null) {
         mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
      }
      View view = inflater.inflate(R.layout.fragment_detail, container, false);
      mDay = (TextView)view.findViewById(R.id.day);
      mDate = (TextView)view.findViewById(R.id.date);
      mHigh = (TextView)view.findViewById(R.id.high);
      mLow = (TextView)view.findViewById(R.id.low);
      mImage = (ImageView)view.findViewById(R.id.image);
      mForecast = (TextView)view.findViewById(R.id.forecast);
      mHumidity = (TextView)view.findViewById(R.id.humidity);
      mWind = (TextView)view.findViewById(R.id.wind);
      mPressure = (TextView)view.findViewById(R.id.pressure);
      return view;
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      // inflate the menu; this adds items to the action bar if it's present
      inflater.inflate(R.menu.detail_fragment, menu);
      // retrieve the share menu item
      MenuItem item = menu.findItem(R.id.action_share);
      // Fetch reference to the share action provider
      mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
      // If onLoadFinished happens before this, we can go ahead and set the share intent now.
      if (mForecastText != null)
         mShareActionProvider.setShareIntent(createShareForecastIntent());
   }

   private Intent createShareForecastIntent() {
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      intent.setType("text/plain");
      intent.putExtra(Intent.EXTRA_TEXT, mForecastText + " #SunshineApp");
      return intent;
   }

   @Override
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      getLoaderManager().initLoader(DETAIL_LOADER, null, this);
      super.onActivityCreated(savedInstanceState);
   }

   public void onLocationChanged( String newLocation ) {
      // replace the uri, since the location has changed
      Uri uri = mUri;
      if (null != uri) {
         long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
         Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
         mUri = updatedUri;
         getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
      }
   }

   @Override
   public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
      if ( null != mUri ) {
         // Now create and return a CursorLoader that will take care of creating a Cursor for the
         // data being displayed.
         return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
      }
      return null;
   }

   @Override
   public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      if (cursor != null && cursor.moveToFirst()) {
         // read weather condition ID from cursor
         int weatherId = cursor.getInt(COL_WEATHER_CONDITION_ID);
         // use weather art image
         int resId = Utility.getArtResourceForWeatherCondition(weatherId);
         mImage.setImageResource(resId);
         // read data from cursor and update views for date of week and date
         long date = cursor.getLong(COL_WEATHER_DATE);
         String friendlyDateText = Utility.getDayName(getActivity(), date);
         String dateText = Utility.getFormattedMonthDay(getActivity(), date);
         mDay.setText(friendlyDateText);
         mDate.setText(dateText);
         // Read description from cursor and update view
         String description = cursor.getString(COL_WEATHER_DESC);
         mForecast.setText(description);
         // Read high temperature from cursor and update view
         boolean isMetric = Utility.isMetric(getActivity());
         double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);
         String highString = Utility.formatTemperature(getActivity(), high, isMetric);
         mHigh.setText(highString);
         // Read low temperature from cursor and update view
         double low = cursor.getDouble(COL_WEATHER_MIN_TEMP);
         String lowString = Utility.formatTemperature(getActivity(), low, isMetric);
         mLow.setText(lowString);
         // Read humidity from cursor and update view
         float humidity = cursor.getFloat(COL_WEATHER_HUMIDITY);
         mHumidity.setText(getActivity().getString(R.string.format_humidity, humidity));
         // Read wind speed and direction from cursor and update view
         float windSpeedStr = cursor.getFloat(COL_WEATHER_WIND_SPEED);
         float windDirStr = cursor.getFloat(COL_WEATHER_DEGREES);
         mWind.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));
         // Read pressure from cursor and update view
         float pressure = cursor.getFloat(COL_WEATHER_PRESSURE);
         mPressure.setText(getActivity().getString(R.string.format_pressure, pressure));
         // We still need this for the share intent
         mForecastText = String.format("%s - %s - %s/%s", dateText, description, high, low);
         // If onCreateOptionsMenu has already happened, we need to update the share intent now.
         if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
         }
      }
   }

   @Override
   public void onLoaderReset(Loader<Cursor> loader) {
   }
}
