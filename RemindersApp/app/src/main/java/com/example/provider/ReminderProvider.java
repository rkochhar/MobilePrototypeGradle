package com.example.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ReminderProvider extends ContentProvider
{
    RemindersDatabaseHelper remindersDatabaseHelper;

    public static final int ROUTE_ENTRIES = 1;

    public static final int ROUTE_ENTRIES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ReminderContract.CONTENT_AUTHORITY, "entries", ROUTE_ENTRIES);
        uriMatcher.addURI(ReminderContract.CONTENT_AUTHORITY, "entries/*", ROUTE_ENTRIES_ID);
    }

    @Override
    public boolean onCreate() {
        remindersDatabaseHelper = new RemindersDatabaseHelper(getContext());
        remindersDatabaseHelper.onCreate(remindersDatabaseHelper.getReadableDatabase());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selection, String[] selectionArgs, String sortOder) {
        SQLiteDatabase db = remindersDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriMatch = uriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_ENTRIES_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        "_ID" + " = " + id);
                return queryBuilder.query(db, projections, selection, selectionArgs, null, null, null );

            case ROUTE_ENTRIES:
                // Return all known entries.
                return db.query(ReminderContract.Entry.TABLE_NAME, projections, selection, selectionArgs, null, null, null );
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ROUTE_ENTRIES:
                return ReminderContract.Entry.CONTENT_TYPE;
            case ROUTE_ENTRIES_ID:
                return ReminderContract.Entry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    static class RemindersDatabaseHelper extends SQLiteOpenHelper
    {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "reminders.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ReminderContract.Entry.TABLE_NAME + " (" +
                        ReminderContract.Entry._ID + " INTEGER PRIMARY KEY," +
                        ReminderContract.Entry.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA_SEP +
                        ReminderContract.Entry.COLUMN_NAME_NAME    + TYPE_TEXT + COMMA_SEP +
                        ReminderContract.Entry.COLUMN_NAME_DATE + TYPE_TEXT + COMMA_SEP +
                        ReminderContract.Entry.COLUMN_NAME_TYPE + TYPE_INTEGER + COMMA_SEP +
                        ReminderContract.Entry.COLUMN_NAME_SET + " BOOLEAN" +")";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ReminderContract.Entry.TABLE_NAME;

        public RemindersDatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
            ContentValues values = new ContentValues();
            values.put(ReminderContract.Entry.COLUMN_NAME_ENTRY_ID, "1");
            values.put(ReminderContract.Entry.COLUMN_NAME_NAME, "Record 1");
            values.put(ReminderContract.Entry.COLUMN_NAME_DATE, "1987-01-01");
            values.put(ReminderContract.Entry.COLUMN_NAME_TYPE, 1);
            values.put(ReminderContract.Entry.COLUMN_NAME_SET, false);
            sqLiteDatabase.insert(ReminderContract.Entry.TABLE_NAME, "", values);

            values.clear();
            values.put(ReminderContract.Entry.COLUMN_NAME_ENTRY_ID, "2");
            values.put(ReminderContract.Entry.COLUMN_NAME_NAME, "Record 2");
            values.put(ReminderContract.Entry.COLUMN_NAME_DATE, "1990-03-06");
            values.put(ReminderContract.Entry.COLUMN_NAME_TYPE, 2);
            values.put(ReminderContract.Entry.COLUMN_NAME_SET, false);
            sqLiteDatabase.insert(ReminderContract.Entry.TABLE_NAME, "", values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            onCreate(sqLiteDatabase);
        }
    }
}
