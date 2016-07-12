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
   private final int VIEW_TYPE_TODAY = 0;
   private final int VIEW_TYPE_FUTURE_DAY = 1;

   public ForecastAdapter(Context context, Cursor c, int flags) {
      super(context, c, flags);
   }

   // Remember that these views are reused as needed.
   // Remember that adapters work with listviews to populate them. They create duplicates of the same
   // layout to put into the list view. This is where you return what layout is going to be duplicated
   @Override
   public View newView(Context context, Cursor cursor, ViewGroup parent) {
      // choose the layout type
      int viewType = getItemViewType(cursor.getPosition());
      int layoutId = viewType == VIEW_TYPE_TODAY ? R.layout.list_item_forecast_today : R.layout.list_item_forecast;
      // In our case, we’re inflating our listview layout, list_item_forecast, and then returning it
      View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
      ViewHolder holder = new ViewHolder(view);
      view.setTag(holder);
      return view;
   }

   // This is where we fill-in the views with the contents of the cursor.
   // This is where the exciting bit occurs: you are binding the values in the cursor to the view
   @Override
   public void bindView(View view, Context context, Cursor cursor) {
      ViewHolder holder = (ViewHolder)view.getTag();
      // Use placeholder image for now
      holder.image.setImageResource(R.drawable.ic_launcher);
      // TODO Read date from cursor
      long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
      holder.date.setText(Utility.getFriendlyDayString(context, dateInMillis));
      // TODO Read weather forecast from cursor
      holder.forecast.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));
      // Read user preference for metric or imperial temperature units
      boolean isMetric = Utility.isMetric(context);
      // Read high temperature from cursor
      double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
      holder.high.setText(Utility.formatTemperature(context, high, isMetric));
      // TODO Read low temperature from cursor
      double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
      holder.low.setText(Utility.formatTemperature(context, low, isMetric));
   }

   @Override
   public int getViewTypeCount() {
      return 2;
   }

   @Override
   public int getItemViewType(int position) {
      return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
   }

   public static class ViewHolder {
      public final ImageView image;
      public final TextView date;
      public final TextView forecast;
      public final TextView high;
      public final TextView low;

      public ViewHolder(View view) {
         image = (ImageView)view.findViewById(R.id.list_item_icon);
         date = (TextView)view.findViewById(R.id.list_item_date_textview);
         forecast = (TextView)view.findViewById(R.id.list_item_forecast_textview);
         high = (TextView)view.findViewById(R.id.list_item_high_textview);
         low = (TextView)view.findViewById(R.id.list_item_low_textview);
      }
   }
}

