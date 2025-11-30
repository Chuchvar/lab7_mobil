package com.example.lab7;

import android.content.ContentUris;
import android.net.Uri;

public class FriendsContract {
    static final String TABLE_NAME = "friends";

    static final String CONTENT_AUTHORITY = "com.example.friendsprovider";

    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." +
            CONTENT_AUTHORITY + "." + TABLE_NAME;

    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." +
            CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static class Columns {
        public static final String _ID = "_id";
        public static final String NAME = "Name";
        public static final String EMAIL = "Email";
        public static final String PHONE = "Phone";

        private Columns() {
        }
    }

    static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_AUTHORITY_URI, TABLE_NAME);

    static Uri buildFriendUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }
    static long getFriendId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}