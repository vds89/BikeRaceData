package com.example.pcvincenzo.bikeracedata.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pcvincenzo on 15/01/18.
 */

public class RaceContract {

    /* Inner class that defines the table contents of the races table */
    public static final class RaceEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "races";

        // Column with the id key into the races table.
        public static final String _ID = BaseColumns._ID;
        // Column with the race name.
        public static final String COLUMN_RACE_LOCATION = "location";
        // Column with the race name.
        public static final String COLUMN_RACE_DATE = "date";
        // Column with the race breed.
        public static final String COLUMN_RACE_DURATION = "duration";
        // Column with the race gender.
        public static final String COLUMN_RACE_DISTANCE = "distance";
        // Column with the race weight.
        public static final String COLUMN_RACE_ELEVATION = "elevation";


        /** Complete CONTENT_URI - inside each of the Entry classes in the contract,
         * we create a full URI for the class as a constant called CONTENT_URI.
         * The Uri.withAppendedPath() method appends the BASE_CONTENT_URI (which contains the scheme
         * and the content authority) to the path segment. */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RACES);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of races.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RACES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single race.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RACES;
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.pcvincenzo.bikeracedata";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.races/races/ is a valid path for
     * looking at race data. content://com.example.android.races/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_RACES = "races";


}
