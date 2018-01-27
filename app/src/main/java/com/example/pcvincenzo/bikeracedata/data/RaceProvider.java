package com.example.pcvincenzo.bikeracedata.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.pcvincenzo.bikeracedata.data.RaceContract.CONTENT_AUTHORITY;
import static com.example.pcvincenzo.bikeracedata.data.RaceContract.PATH_RACES;
import static com.example.pcvincenzo.bikeracedata.data.RaceContract.RaceEntry._ID;

/**
 * Created by pcvincenzo on 18/01/18.
 */

public class RaceProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = RaceProvider.class.getSimpleName();

    /** Database Helper object */
    private RaceDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a raceDbHelper object to gain access to the races database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new RaceDbHelper(getContext());

        return true;
    }


    /**
     * Perform the query for the given URI. Use the given projection, selection,
     * selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
// Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case RACES:
                // For the RACES code, query the races table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the races table.
                // TODO: Perform database query on races table
                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                // This will perform a query on entire table return a
                // Cursor containing .
                cursor = database.query(RaceContract.RaceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case RACE_ID:
                // For the RACE_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.races/races/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the races table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(RaceContract.RaceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RACES:
                return insertRace(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a race into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertRace(Uri uri, ContentValues values) {

        // Check that the name is not null
        String location = values.getAsString(RaceContract.RaceEntry.COLUMN_RACE_LOCATION);
        if (location == null) {
            throw new IllegalArgumentException("Race requires a location");
        }

        // Check that the name is not null
        String date = values.getAsString(RaceContract.RaceEntry.COLUMN_RACE_DATE);
        if (date == null) {
            throw new IllegalArgumentException("Race requires a date");
        }

        // Check that the name is not null
        Integer duration = values.getAsInteger(RaceContract.RaceEntry.COLUMN_RACE_DURATION);
        if (duration == null) {
            throw new IllegalArgumentException("Race requires a duration");
        }

        // Check that the breed is not null
        Integer distance = values.getAsInteger(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE);
        if (distance != null && distance < 0) {
            throw new IllegalArgumentException("race requires valid distance");
        }

        // Check that the breed is not null
        Integer elevation = values.getAsInteger(RaceContract.RaceEntry.COLUMN_RACE_ELEVATION);
        if (elevation == null) {
            throw new IllegalArgumentException("Race requires a elevation");
        }

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(RaceContract.RaceEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (newRowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        if (newRowId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RACES:
                return updateRace(uri, contentValues, selection, selectionArgs);
            case RACE_ID:
                // For the race_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateRace(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update races in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more races).
     * Return the number of rows that were successfully updated.
     */
    private int updateRace(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // TODO: Update the selected races in the races database table with the given ContentValues

        // If the {@link RaceEntry#COLUMN_RACE_LOCATION} key is present,
        // check that the name value is not null.
        if (values.containsKey(RaceContract.RaceEntry.COLUMN_RACE_LOCATION)) {
            String location = values.getAsString(RaceContract.RaceEntry.COLUMN_RACE_LOCATION);
            if (location == null) {
                throw new IllegalArgumentException("race requires a location");
            }
        }
        
        // If the {@link RaceEntry#COLUMN_RACE_DATE} key is present,
        // check that the name value is not null.
        if (values.containsKey(RaceContract.RaceEntry.COLUMN_RACE_DATE)) {
            String date = values.getAsString(RaceContract.RaceEntry.COLUMN_RACE_DATE);
            if (date == null) {
                throw new IllegalArgumentException("race requires a date");
            }
        }
        
        // If the {@link raceEntry#COLUMN_race_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(RaceContract.RaceEntry.COLUMN_RACE_DURATION)) {
            Integer duration = values.getAsInteger(RaceContract.RaceEntry.COLUMN_RACE_DURATION);
            if (duration == null) {
                throw new IllegalArgumentException("Race requires a duration");
            }
        }

        // If the {@link raceEntry#COLUMN_race_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer distance = values.getAsInteger(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE);
            if (distance != null && distance < 0) {
                throw new IllegalArgumentException("race requires valid distance");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) return 0;

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int mRowsUpdated = db.update(
                RaceContract.RaceEntry.TABLE_NAME,    // the table name to update
                values,                             // the columns to update
                selection,                          // the column to select on
                selectionArgs                       // the value to compare to
        );

        if (mRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return mRowsUpdated;
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int mRowsDeleted = 0;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RACES:

                if (mRowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                // Delete all rows that match the selection and selection args
                mRowsDeleted = database.delete(RaceContract.RaceEntry.TABLE_NAME, selection, selectionArgs);

                return mRowsDeleted;

            case RACE_ID:
                // Delete a single row given by the ID in the URI
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                if (mRowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                mRowsDeleted = database.delete(RaceContract.RaceEntry.TABLE_NAME, selection, selectionArgs);

                return mRowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RACES:
                return RaceContract.RaceEntry.CONTENT_LIST_TYPE;
            case RACE_ID:
                return RaceContract.RaceEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    /** URI matcher code for the content URI for the races table */
    private static final int RACES = 100;

    /** URI matcher code for the content URI for a single race in the races table */
    private static final int RACE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_RACES, RACES);
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_RACES + "/#", RACE_ID);
    }

}
