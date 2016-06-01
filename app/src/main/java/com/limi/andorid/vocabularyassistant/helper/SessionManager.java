package com.limi.andorid.vocabularyassistant.helper;

/**
 * Created by limi on 16/4/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "AndroidSessionManager";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_SELECTED = "isSelected";
    private static final String KEY_SELECT_BOOK = "selectBook";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyIsSelected(boolean isSelected) {
        editor.putBoolean(KEY_IS_SELECTED, isSelected);

        // commit changes
        editor.commit();

        Log.d(TAG, "User selected book session modified!");
    }


    public void setKeySelectBook(String selectBook) {
        editor.putString(KEY_SELECT_BOOK, selectBook);

        // commit changes
        editor.commit();

        Log.d(TAG, "User selected book session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isSelected() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}

