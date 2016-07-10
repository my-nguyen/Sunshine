package com.example.android.sunshine.app;

/**
 * Created by My on 7/7/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
   public ForecastAdapter(Context context, Cursor c, int flags) {
      super(context, c, flags);
   }

   /**
    * Prepare the weather high/lows for presentation.
    */
   private String formatHighLows(double high, double low) {
      boolean isMetric = Utility.isMetric(mContext);
      String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
      return highLowStr;
   }

   // This is ported from FetchWeatherTask --- but now we go straight from the cursor to the string.
   // this method takes a row from a cursor and constructs a single string of the format:
   // Date - Weather -- High/Low
   // This is the string we’re used to seeing in the listview element. It uses formatHighLow to get
   // the correct string for the temperature.
   private String convertCursorRowToUXFormat(Cursor cursor) {
      // get row indices for our cursor
      int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
      int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
      int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
      int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

      String highAndLow = formatHighLows(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
            cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

      return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
            " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
            " - " + highAndLow;
   }

   // Remember that these views are reused as needed.
   // Remember that adapters work with listviews to populate them. They create duplicates of the same
   // layout to put into the list view. This is where you return what layout is going to be duplicated
   @Override
   public View newView(Context context, Cursor cursor, ViewGroup parent) {
      // In our case, we’re inflating our listview layout, list_item_forecast, and then returning it
      return LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
   }

   // This is where we fill-in the views with the contents of the cursor.
   // This is where the exciting bit occurs: you are binding the values in the cursor to the view
   @Override
   public void bindView(View view, Context context, Cursor cursor) {
      // our view is pretty simple here --- just a text view
      // we'll keep the UI functional with a simple (and slow!) binding.
      // The View passed into bindView is the View returned from newView. We know it’s a TextView,
      // so we cast it. Then we take the Cursor, run it through our custom made formatting function,
      // and set the text of the TextView.
      TextView tv = (TextView)view;
      tv.setText(convertCursorRowToUXFormat(cursor));
   }
}

