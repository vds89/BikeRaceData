package com.example.pcvincenzo.bikeracedata;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by pcvincenzo on 15/01/18.
 */

public class RaceCursorAdapter extends CursorAdapter {

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
        TextView raceDayTextView = (TextView) view.findViewById(R.id.race_day);
        TextView raceDistanceTextView = (TextView) view.findViewById(R.id.race_distance);
        TextView racePlaceTextView = (TextView) view.findViewById(R.id.race_place);
        TextView raceTimeTextView = (TextView) view.findViewById(R.id.race_time);
        TextView raceElevationTextView = (TextView) view.findViewById(R.id.race_elevation);

        // Update the TextViews with the attributes for the current pet
        raceDayTextView.setText("15 Gennaio 2018");
        raceDistanceTextView.setText("120 km");
        racePlaceTextView.setText("Alassio");
        raceTimeTextView.setText("02:30:00");
        raceElevationTextView.setText("1500 m");
    }


}
