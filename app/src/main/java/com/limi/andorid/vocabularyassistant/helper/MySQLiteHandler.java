package com.limi.andorid.vocabularyassistant.helper;

/**
 * Created by limi on 16/4/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.limi.andorid.vocabularyassistant.data.UserWord;

import java.util.ArrayList;
import java.util.HashMap;

public class MySQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android";

    // Login table name
    private static final String TABLE_USER = "user";

    // User word table name
    private static final String TABLE_USERWORD = "userWord";


    // Login Table Columns names

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_CREATED_AT = "created_at";

    // User name Table Columns names

    private static final String KEY_WORD_ID = "wordID";
    //    private static final String KEY_USER_ID = "userID";
    //    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_FAV = "is_favourite";
    private static final String KEY_WRONG_TIME = "wrong_time";
    private static final String KEY_WORD_BASE = "word_base";

    public MySQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER ," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";


        String CREATE_USERWORD_TABLE = "CREATE TABLE " + TABLE_USERWORD + "("
                + KEY_WORD_ID + " INTEGER ," + KEY_USER_ID + " INTEGER,"
                + KEY_CREATED_AT + " TEXT," + KEY_FAV + " INTEGER, " + KEY_WRONG_TIME + " INTEGER, "
                + KEY_WORD_BASE + " TEXT" +
                ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_USERWORD_TABLE);

        Log.d(TAG, "User Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERWORD);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String name, String email, int uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, uid); //userID
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addUserWord(UserWord userWord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD_ID, userWord.getWordID()); //wordID
        values.put(KEY_USER_ID, userWord.getUserID()); // userID
        values.put(KEY_CREATED_AT, userWord.getCreateDate()); // date
        values.put(KEY_FAV, userWord.isFavourite() ? 1 : 0); //wordID
        values.put(KEY_WRONG_TIME, userWord.getWrongTime()); // userID
        values.put(KEY_WORD_BASE, userWord.getWordBase()); // date

        // Inserting Row
        long id = db.insert(TABLE_USERWORD, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New userword inserted into sqlite: " + id);
    }

    public boolean isWordExist(UserWord word) {
        String sUserID = String.valueOf(word.getUserID());
        String sWordID = String.valueOf(word.getWordID());

        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_WORD_ID + " = ?" + " AND " + KEY_USER_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sWordID, sUserID});
        cursor.moveToFirst();

        cursor.close();
        db.close();
        return cursor.getCount() > 0;

    }

    public void increaseWrongTime(UserWord word) {

        String sUserID = String.valueOf(word.getUserID());
        String sWordID = String.valueOf(word.getWordID());

        String selectQuery = "SELECT " + KEY_WRONG_TIME + " FROM " + TABLE_USERWORD + " WHERE " + KEY_WORD_ID + " = ?" + " AND " + KEY_USER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sWordID, sUserID});
        int wrongTime = -1;
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            wrongTime = cursor.getInt(0);
        }
        wrongTime++;

        cursor.close();

        String updateQuery = "UPDATE " + TABLE_USERWORD + " SET " + KEY_WRONG_TIME + " = ? " + " WHERE " + TABLE_USERWORD + "." + KEY_WORD_ID + " = ? AND " + TABLE_USERWORD + "." + KEY_USER_ID + " = ?";


        ContentValues cv = new ContentValues();
        cv.put(KEY_WRONG_TIME, wrongTime);
        String[] args = {sWordID, sUserID};
        db.update(TABLE_USERWORD, cv, "wordID = ? AND userID = ?", args);
//        Cursor cursor1 = db.rawQuery(updateQuery, new String[]{String.valueOf(favourite), sWordID, sUserID});
//        cursor1.close();

        db.close();

    }

    public void changeFav(UserWord word) {

        String sUserID = String.valueOf(word.getUserID());
        String sWordID = String.valueOf(word.getWordID());

        String selectQuery = "SELECT " + KEY_FAV + " FROM " + TABLE_USERWORD + " WHERE " + KEY_WORD_ID + " = ?" + " AND " + KEY_USER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sWordID, sUserID});
        int favourite = -1;
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            favourite = cursor.getInt(0);
        }
        if (favourite == 0) {
            favourite = 1;
        } else if (favourite == 1) {
            favourite = 0;
        }

        cursor.close();

        String updateQuery = "UPDATE " + TABLE_USERWORD + " SET " + KEY_FAV + " = ? " + " WHERE " + TABLE_USERWORD + "." + KEY_WORD_ID + " = ? AND " + TABLE_USERWORD + "." + KEY_USER_ID + " = ?";


        ContentValues cv = new ContentValues();
        cv.put(KEY_FAV, favourite);
        String[] args = {sWordID, sUserID};
        db.update(TABLE_USERWORD, cv, "wordID = ? AND userID = ?", args);
//        Cursor cursor1 = db.rawQuery(updateQuery, new String[]{String.valueOf(favourite), sWordID, sUserID});
//        cursor1.close();

        db.close();

    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("userID", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("created_at", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /**
     * Getting word base data from database
     */
    public ArrayList<UserWord> getUserWordData(int userID) {
        String sUserID = String.valueOf(userID);
        ArrayList<UserWord> userWordArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_USER_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sUserID});
        // Move to first row
        cursor.moveToFirst();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            boolean isfavourite = (cursor.getInt(3) == 1);
            UserWord userWord = new UserWord(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), isfavourite, cursor.getInt(4), cursor.getString(5));
            userWordArrayList.add(userWord);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user word from Sqlite: " + userWordArrayList.toString());

        return userWordArrayList;
    }

    public int getUserWordCount(int userID) {
        String sUserID = String.valueOf(userID);
        ArrayList<UserWord> userWordArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_USER_ID + " = ?";
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sUserID});
        // Move to first row
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }


        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user word from Sqlite: " + userWordArrayList.toString());

        return count;
    }

    public int getUserWordDateCount(int userID, String date) {
        String sUserID = String.valueOf(userID);
        ArrayList<UserWord> userWordArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_USER_ID + " = ?" + " AND " + KEY_CREATED_AT + " = " + " ? ";
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sUserID, date});
        // Move to first row
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }


        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user word from Sqlite: " + userWordArrayList.toString());

        return count;
    }

    public int getUserWrongDateCount(int userID, String date) {
        String sUserID = String.valueOf(userID);
        ArrayList<UserWord> userWordArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_USER_ID + " = ?" + " AND " + KEY_CREATED_AT + " = " + " ? " + " AND " + KEY_WRONG_TIME + " >= 1";
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sUserID, date});
        // Move to first row
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }


        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user word from Sqlite: " + userWordArrayList.toString());

        return count;
    }

    public int getUserWrongCount(int userID) {
        String sUserID = String.valueOf(userID);
        ArrayList<UserWord> userWordArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERWORD + " WHERE " + KEY_USER_ID + " = ?" + " AND " + KEY_WRONG_TIME + " >= 1";
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sUserID});
        // Move to first row
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }


        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user word from Sqlite: " + userWordArrayList.toString());

        return count;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUserWord() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
//        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
