package com.example.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ReminderContract {
    public static final String CONTENT_AUTHORITY = "com.example.provider.ReminderProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_ENTRIES = "entries";

    public static class Entry implements BaseColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/remindersapp.entries";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/remindersapp.entry";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        public static final String TABLE_NAME = "reminders";

        public static final String COLUMN_NAME_ENTRY_ID = "reminder_id";

        public static final String COLUMN_NAME_NAME = "name";

        public static final String COLUMN_NAME_DATE = "date";

        public static final String COLUMN_NAME_TYPE = "type";

        public static final String COLUMN_NAME_SET = "alarm";

        public static final String COLUMN_NAME_GROUP = "reminder_group";

        public static final String COLUMN_NAME_IS_LOCAL = "is_local";
    }
}
