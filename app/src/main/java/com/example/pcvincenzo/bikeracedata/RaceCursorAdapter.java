package com.example.pcvincenzo.bikeracedata;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pcvincenzo.bikeracedata.data.RaceContract;

/**
 * Created by pcvincenzo on 15/01/18.
 */

public class RaceCursorAdapter extends CursorAdapter {

    /** Tag for the log messages */
    public static final String LOG_TAG = RaceCursorAdapter.class.getSimpleName();
    /**
     * Constructs a new {@link RaceCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public RaceCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView raceLocationTextView = (TextView) view.findViewById(R.id.race_place);
        TextView raceDateTextView = (TextView) view.findViewById(R.id.race_day);
        TextView raceDurationTextView = (TextView) view.findViewById(R.id.race_time);
        TextView raceDistanceTextView = (TextView) view.findViewById(R.id.race_distance);
        TextView raceElevationTextView = (TextView) view.findViewById(R.id.race_elevation);

        //if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int idColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry._ID);
            int locationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_LOCATION);
            int dateColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DATE);
            int durationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DURATION);
            int distanceColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE);

//        Log.d(LOG_TAG, "==============> distanceColumnIndex" + distanceColumnIndex);
            int elevationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_ELEVATION);

            // Extract out the value from the Cursor for the given column index
            String location = cursor.getString(locationColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String duration = cursor.getString(durationColumnIndex);
            int distance = cursor.getInt(distanceColumnIndex);
            Log.d(LOG_TAG, "==============> distance" + distance);

            int elevation = cursor.getInt(elevationColumnIndex);

            // Update the TextViews with the attributes for the current pet
            raceLocationTextView.setText(location);
            raceDateTextView.setText(date);
            raceDurationTextView.setText(duration);
            raceDistanceTextView.setText(Integer.toString(distance));
            raceElevationTextView.setText(Integer.toString(elevation));
        //}
    }
}
