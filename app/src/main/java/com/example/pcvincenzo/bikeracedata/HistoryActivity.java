package com.example.pcvincenzo.bikeracedata;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcvincenzo.bikeracedata.data.RaceContract;
import com.example.pcvincenzo.bikeracedata.data.RaceDbHelper;

import static com.example.pcvincenzo.bikeracedata.data.RaceContract.RaceEntry.COLUMN_RACE_DISTANCE;
import static com.example.pcvincenzo.bikeracedata.data.RaceContract.RaceEntry.COLUMN_RACE_ELEVATION;
import static com.example.pcvincenzo.bikeracedata.data.RaceContract.RaceEntry.CONTENT_URI;
import static com.example.pcvincenzo.bikeracedata.data.RaceContract.RaceEntry.TABLE_NAME;

public class HistoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = HistoryActivity.class.getSimpleName();

    // Identifies a particular Loader being used in this component
    private static final int RACE_LOADER = 0;


    // This is the Adapter being used to display the list's data
    RaceCursorAdapter mAdapter;

    /** Database Helper object */
    private RaceDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the race data
        ListView raceListView = (ListView) findViewById(R.id.list);


        TextView totaldistanceTextView = (TextView) findViewById(R.id.km_tot_sum);
        TextView totalSessionsTextView = (TextView) findViewById(R.id.session_tot);
        TextView totalElevationTextView = (TextView) findViewById(R.id.elevation_tot);

        totaldistanceTextView.setText(Integer.toString(getDistanceColumnSum()));
        totalSessionsTextView.setText(Long.toString(getSessionsColumnSum()));
        totalElevationTextView.setText(Integer.toString(getElevationColumnSum()));


        // Setup cursor adapter using null cursor
        mAdapter = new RaceCursorAdapter(this, null);
        // Sets the adapter for the view
        raceListView.setAdapter(mAdapter);

        //Set up the click listener
        raceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //When a Pet in the list is clicked on, open its Edit Tab
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Get the Intent that started Editor activity
                Intent intent = new Intent(HistoryActivity.this, EditorActivity.class);

                //Form the content URI that represent the specific pet that was clicked on,
                //by appending the "id" (passed as an input to this method) onto the
                //{@link PetEntry#CONTENT_URI}.
                //For example, the URI will be content://com.example.android.pets/pets/2
                //if the pet with ID=2 was clicked on
                intent.setData(ContentUris.withAppendedId(CONTENT_URI, id));

                // Send the intent to launch a new activity
                startActivity(intent);
            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(RACE_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    private void insertRace(){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(RaceContract.RaceEntry.COLUMN_RACE_LOCATION, "Torino");
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DATE, "20 Gennaio 2018");
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DURATION, "2");
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE, "120");
        values.put(RaceContract.RaceEntry.COLUMN_RACE_ELEVATION, "2300");

        // Insert the new row, returning the primary key value of the new row
        //long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);

        // Defines a new Uri object that receives the result of the insertion
        Uri mNewUri = getContentResolver().insert(
                CONTENT_URI,                        // the user PetEntry content URI
                values                              // the values to insert
        );
        if (mNewUri != null){
            // The insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_race_successful),
                    Toast.LENGTH_SHORT).show();
        }
        //Log.v("CatalogActivity", "New row ID " + newRowId);
    }

    private void deleteAllRaces(){

        // Call the ContentResolver to delete ALL the pet in the table.

        // Deletes the pet that match the selection criteria
        int mRowsDeleted = getContentResolver().delete(
                RaceContract.RaceEntry.CONTENT_URI,     // the user dictionary content URI
                null,                                   // the column to select on
                null                                    // the value to compare to
        );

        // Show a toast message depending on whether or not the update was successful.
        if (mRowsDeleted == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.editor_delete_race_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_deleteAll_race_successful),
                    Toast.LENGTH_SHORT).show();

            getContentResolver().notifyChange(CONTENT_URI,null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertRace();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllRaces();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] PROJECTION = {
                RaceContract.RaceEntry._ID,
                RaceContract.RaceEntry.COLUMN_RACE_LOCATION,
                RaceContract.RaceEntry.COLUMN_RACE_DATE,
                RaceContract.RaceEntry.COLUMN_RACE_DURATION,
                RaceContract.RaceEntry.COLUMN_RACE_DISTANCE,
                RaceContract.RaceEntry.COLUMN_RACE_ELEVATION
        };

            /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case RACE_LOADER:
                // Now create and return a CursorLoader that will take care of
                // creating a Cursor for the data being displayed.
                return new CursorLoader(this, CONTENT_URI,
                        PROJECTION, null, null, null);

            default:
                // An invalid id was passed in
                return null;
        }

    }


    @Override
    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(cursor);

    }

    @Override
    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    public int getDistanceColumnSum() {

        mDbHelper = new RaceDbHelper(this);

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT SUM(" + COLUMN_RACE_DISTANCE + ") as TotalDistance FROM "
                + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            int total = cursor.getInt(cursor.getColumnIndex("TotalDistance"));// get final total

            Log.d(LOG_TAG,"=================> TOTAL DISTANCE= " + total);
            return total;
        }
        return 0;
    }

    public long getSessionsColumnSum() {

        mDbHelper = new RaceDbHelper(this);

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, TABLE_NAME);
        Log.d(LOG_TAG,"=================> TOTAL RACES= " + count);
        return count;
    }

    public int getElevationColumnSum() {

        mDbHelper = new RaceDbHelper(this);

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT SUM(" + COLUMN_RACE_ELEVATION + ") as TotalElevation FROM "
                + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            int total = cursor.getInt(cursor.getColumnIndex("TotalElevation"));// get final total

            Log.d(LOG_TAG,"=================> TOTAL ELEVATION= " + total);
            return total;
        }
        return 0;
    }
}
