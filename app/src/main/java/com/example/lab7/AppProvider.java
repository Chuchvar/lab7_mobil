package com.example.lab7;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class AppProvider extends ContentProvider {

    private AppDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int FRIENDS = 100;
    public static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FriendsContract.CONTENT_AUTHORITY, FriendsContract.TABLE_NAME, FRIENDS);
        matcher.addURI(FriendsContract.CONTENT_AUTHORITY, FriendsContract.TABLE_NAME + "/#", FRIENDS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case FRIENDS:
                queryBuilder.setTables(FriendsContract.TABLE_NAME);
                break;
            case FRIENDS_ID:
                queryBuilder.setTables(FriendsContract.TABLE_NAME);
                long taskId = FriendsContract.getFriendId(uri);
                queryBuilder.appendWhere(FriendsContract.Columns._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FRIENDS:
                return FriendsContract.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        if (match == FRIENDS) {
            db = mOpenHelper.getWritableDatabase();
            recordId = db.insert(FriendsContract.TABLE_NAME, null, values);

            if (recordId > 0) {
                returnUri = FriendsContract.buildFriendUri(recordId);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(returnUri, null);
                }
            } else {
                throw new SQLException("Failed to insert: " + uri.toString());
            }
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if (match != FRIENDS && match != FRIENDS_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if (match == FRIENDS_ID) {
            long taskId = FriendsContract.getFriendId(uri);
            selectionCriteria = FriendsContract.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(FriendsContract.TABLE_NAME, selectionCriteria, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if (match != FRIENDS && match != FRIENDS_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if (match == FRIENDS_ID) {
            long taskId = FriendsContract.getFriendId(uri);
            selectionCriteria = FriendsContract.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.update(FriendsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
    }
}