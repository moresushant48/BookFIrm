package com.example.bookfirm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bookfirm.R;
import com.example.bookfirm.models.User;

import java.util.ArrayList;
import java.util.Objects;

public class UserDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookfirm";
    private static final String TABLE_NAME = "users";

    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_ADDRESS = "address";
    private static final String COL_MOBILENO = "mobileno";
    private static final String COL_PASSWORD = "password";
    private static final String[] COLUMNS = {COL_ID, COL_USERNAME, COL_EMAIL, COL_ADDRESS, COL_MOBILENO, COL_PASSWORD};

    public UserDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, R.string.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USERNAME + " TEXT, "
                + COL_EMAIL + " TEXT, "
                + COL_ADDRESS + " TEXT,"
                + COL_MOBILENO + " TEXT,"
                + COL_PASSWORD + " TEXT)";

        Log.e("Inside OnCreate.", CREATION_TABLE);

        sqLiteDatabase.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);

    }

    public void deleteOne(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ? ", new String[]{String.valueOf(user.getId())});
        db.close();

    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " id = ?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User();
        user.setId(cursor.getInt(0));
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setAddress(cursor.getString(3));
        user.setMobileno(cursor.getString(4));
        user.setPassword(cursor.getString(5));

        cursor.close();
        return user;
    }

    public ArrayList<User> allUsers() {

        ArrayList<User> users = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user;

        if (cursor.moveToFirst()) {
            do {

                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setMobileno(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                users.add(user);

            } while (cursor.moveToNext());

        }

        cursor.close();
        return users;
    }

    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_ADDRESS, user.getAddress());
        values.put(COL_MOBILENO, user.getMobileno());
        values.put(COL_PASSWORD, user.getPassword());
        db.insert(TABLE_NAME, null, values);

        db.close();

    }

    public User login(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " email = ? AND password = ?", new String[]{email, password},
                null, null, null, null);

        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setAddress(cursor.getString(3));
            user.setMobileno(cursor.getString(4));
            user.setPassword(cursor.getString(5));
        }

        Objects.requireNonNull(cursor).close();
        return user;

    }
}