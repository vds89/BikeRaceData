package com.example.pcvincenzo.bikeracedata;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.pcvincenzo.bikeracedata.data.RaceContract;

public class HistoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Identifies a particular Loader being used in this component
    private static final int RACE_LOADER = 0;

    // This is the Adapter being used to display the list's data
    RaceCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.race_list);

        // Find the ListView which will be populated with the race data
        ListView raceListView = (ListView) findViewById(R.id.list);

        // Setup cursor adapter using null cursor
        mAdapter = new RaceCursorAdapter(this, null);
        // Sets the adapter for the view
        raceListView.setAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(RACE_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] PROJECTION = {
                RaceContract.RaceEntry._ID,
                RaceContract.RaceEntry.COLUMN_RACE_LOCATION,
                RaceContract.RaceEntry.COLUMN_RACE_DURATION,
                RaceContract.RaceEntry.COLUMN_RACE_ELEVATION
        };

            /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case RACE_LOADER:
                // Now create and return a CursorLoader that will take care of
                // creating a Cursor for the data being displayed.
                return new CursorLoader(this, RaceContract.RaceEntry.CONTENT_URI,
                        PROJECTION, null, null, null);

            default:
                // An invalid id was passed in
                return null;
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
