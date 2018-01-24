package com.example.pcvincenzo.bikeracedata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pcvincenzo.bikeracedata.data.RaceContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by pcvincenzo on 15/01/18.
 */

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for the log messages */
    public static final String LOG_TAG = EditorActivity.class.getSimpleName();

    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_RACE_LOADER = 0;

    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentRaceUri;

    /**
     * EditText field to enter the race location
     */
    private EditText mLocationEditText;

    /**
     * EditText field to enter the race date
     */
    private EditText mDateEditText;

    /**
     * EditText field to enter the race duration
     */
    private EditText mDurationEditText;


    /**
     * EditText field to enter the race distance
     */
    private EditText mDistanceEditText;

    /**
     * EditText field to enter the race elevation
     */
    private EditText mElevationEditText;

    /**
     * Boolean flag that keeps track of whether the
     * pet has been edited (true) or not (false)
     */
    private boolean mRaceHasChanged = false;

    /**
     * Calendar field to enter the race data
     */
    private Calendar myCalendar = Calendar.getInstance();


    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mRaceHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentRaceUri = intent.getData();

        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentRaceUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_race));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(getString(R.string.editor_activity_title_edit_race));

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_RACE_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mLocationEditText = (EditText) findViewById(R.id.edit_race_location);
        mDateEditText = (EditText) findViewById(R.id.edit_race_date);
        mDurationEditText = (EditText) findViewById(R.id.edit_race_duration);
        mDistanceEditText = (EditText) findViewById(R.id.edit_race_distance);
        mElevationEditText = (EditText) findViewById(R.id.edit_race_elevation);


        mDateEditText.setOnClickListener(new View.OnClickListener() {


        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
                mDateEditText.setText(sdf.format(myCalendar.getTime()));
            }

        };

        @Override
            public void onClick(View view) {
                new DatePickerDialog(EditorActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mLocationEditText.setOnTouchListener(mTouchListener);
        mDurationEditText.setOnTouchListener(mTouchListener);
        mDateEditText.setOnTouchListener(mTouchListener);
        mDistanceEditText.setOnTouchListener(mTouchListener);
        mElevationEditText.setOnTouchListener(mTouchListener);



    }

    /**
     * Get user input from editor and save pet into database.
     */
    private void saveRace() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String locationString = mLocationEditText.getText().toString().trim();
        String dateString = mDateEditText.getText().toString().trim();
        String distanceString = mDistanceEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();
        String elevationString = mElevationEditText.getText().toString().trim();

        //Return if no data are inserted in the mandatory TextEdit fields
        if (locationString.isEmpty() || distanceString.isEmpty()
                || elevationString.isEmpty() || durationString.isEmpty()) {
            Toast.makeText(this, getString(R.string.editor_update_missing_text),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int distance = 0;
        if (!TextUtils.isEmpty(distanceString)) {
            distance = Integer.parseInt(distanceString);
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(RaceContract.RaceEntry.COLUMN_RACE_LOCATION, locationString);
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DATE, dateString);
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DURATION, durationString);
        values.put(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE, distanceString);
        values.put(RaceContract.RaceEntry.COLUMN_RACE_ELEVATION, elevationString);

        // Determine if this is a new or existing race by checking if mCurrentPetUri is null or not
        if (mCurrentRaceUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(RaceContract.RaceEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_update_race_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_race_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentRaceUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_race_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_race_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentRaceUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveRace();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //Trigger the delete confirmation dialog
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mRaceHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mRaceHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                RaceContract.RaceEntry._ID,
                RaceContract.RaceEntry.COLUMN_RACE_LOCATION,
                RaceContract.RaceEntry.COLUMN_RACE_DATE,
                RaceContract.RaceEntry.COLUMN_RACE_DURATION,
                RaceContract.RaceEntry.COLUMN_RACE_DISTANCE,
                RaceContract.RaceEntry.COLUMN_RACE_ELEVATION};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentRaceUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int locationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_LOCATION);
            int dateColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DATE);
            int durationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DURATION);
            int distanceColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_DISTANCE);
            int elevationColumnIndex = cursor.getColumnIndex(RaceContract.RaceEntry.COLUMN_RACE_ELEVATION);

            // Extract out the value from the Cursor for the given column index
            String location = cursor.getString(locationColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String duration = cursor.getString(durationColumnIndex);
            int distance = cursor.getInt(distanceColumnIndex);
            int elevation = cursor.getInt(elevationColumnIndex);

            // Update the views on the screen with the values from the database
            mLocationEditText.setText(location);
            mDateEditText.setText(date);
            mDurationEditText.setText(duration);
            mDistanceEditText.setText(Integer.toString(distance));
            mElevationEditText.setText(Integer.toString(elevation));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mLocationEditText.setText("");
        mDateEditText.setText("");
        mDurationEditText.setText("");
        mDistanceEditText.setText("");
        mElevationEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteRace();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteRace() {

        // Only perform the delete if this is an existing pet.
        if (mCurrentRaceUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.

            // Deletes the pet that match the selection criteria
            int mRowsDeleted = getContentResolver().delete(
                    mCurrentRaceUri,                     // the user dictionary content URI
                    null,                    // the column to select on
                    null                      // the value to compare to
            );

            // Show a toast message depending on whether or not the update was successful.
            if (mRowsDeleted == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_delete_race_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_race_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }
}