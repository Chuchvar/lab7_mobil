package com.example.lab7;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int LOADER_ID = 1;
    private SimpleCursorAdapter mAdapter;

    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameEditText = findViewById(R.id.edit_text_name);
        mPhoneEditText = findViewById(R.id.edit_text_phone);
        mAddButton = findViewById(R.id.button_add_friend);
        mAddButton.setOnClickListener(this);

        ListView friendsList = findViewById(R.id.friends_list);

        String[] from = {
                FriendsContract.Columns.NAME,
                FriendsContract.Columns.PHONE
        };

        int[] to = {
                R.id.list_friend_name,
                R.id.list_friend_phone
        };

        mAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_friend,
                null,
                from,
                to,
                0
        );

        friendsList.setAdapter(mAdapter);

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add_friend) {
            addNewFriend();
        }
    }

    private void addNewFriend() {
        String name = mNameEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть усі поля.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FriendsContract.Columns.NAME, name);
        values.put(FriendsContract.Columns.PHONE, phone);

        Uri newUri = getContentResolver().insert(FriendsContract.CONTENT_URI, values);

        if (newUri != null) {
            Toast.makeText(this, "Друг доданий!", Toast.LENGTH_SHORT).show();
            mNameEditText.setText("");
            mPhoneEditText.setText("");
        } else {
            Toast.makeText(this, "Помилка додавання друга.", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                FriendsContract.Columns._ID,
                FriendsContract.Columns.NAME,
                FriendsContract.Columns.PHONE
        };

        return new CursorLoader(
                this,
                FriendsContract.CONTENT_URI,
                projection,
                null,
                null,
                FriendsContract.Columns.NAME
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}