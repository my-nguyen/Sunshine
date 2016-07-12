package com.example.android.sunshine.app;

/**
 * Created by My on 7/7/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
      // Read weather icon ID from cursor
      int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
      // Use placeholder image for now
      ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
      iconView.setImageResource(R.drawable.ic_launcher);
      // TODO Read date from cursor
      TextView dateText = (TextView)view.findViewById(R.id.list_item_date_textview);
      long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
      dateText.setText(Utility.getFriendlyDayString(context, dateInMillis));
      // TODO Read weather forecast from cursor
      TextView forecast = (TextView)view.findViewById(R.id.list_item_forecast_textview);
      forecast.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));
      // Read user preference for metric or imperial temperature units
      boolean isMetric = Utility.isMetric(context);
      // Read high temperature from cursor
      double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
      TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
      highView.setText(Utility.formatTemperature(high, isMetric));
      // TODO Read low temperature from cursor
      double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
      TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
      lowView.setText(Utility.formatTemperature(low, isMetric));
   }
}

