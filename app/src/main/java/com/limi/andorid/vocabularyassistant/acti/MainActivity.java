package com.limi.andorid.vocabularyassistant.acti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Word;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.WordImportHandler;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static int currentUserID;
    public static int wordStartID;
    public static int wordEndID;
    public static int bookID;
    String titles[] = {"Home", "My Notebook", "Learning Trace", "Message", "Forms", "Settings"};
    int icon[] = {R.mipmap.icon_home, R.mipmap.icon_notebook, R.mipmap.icon_record2, R.mipmap.icon_message, R.mipmap.icon_top, R.mipmap.icon_settings};
    ResideMenuItem item[] = new ResideMenuItem[titles.length];
    private ResideMenu resideMenu;
    private MySQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookID = 0;


        Button menuBtn = (Button) findViewById(R.id.title_bar_left_menu);
        Button favBtn = (Button) findViewById(R.id.title_bar_right_menu);
        menuBtn.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        try {
            InputStream inputStream1 = getAssets().open("threek.xml");
            WordImportHandler.getDataFromXml(inputStream1, "GRE threek Words");
            inputStream1.close();

            InputStream inputStream2 = getAssets().open("toefl.xml");
            WordImportHandler.getDataFromXml(inputStream2, "TOEFL");
            inputStream2.close();

            Toast.makeText(getApplicationContext(), WordImportHandler.systemWordBaseArrayList.get(Word.getLastID() - 1).toString(), Toast.LENGTH_LONG).show();

            InputStream inputStream3 = getAssets().open("ietls.xml");
            WordImportHandler.getDataFromXml(inputStream3, "IETLS");
            inputStream3.close();
            Word.setLastID();

        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Word> words = WordImportHandler.systemWordBaseArrayList;

        switch (bookID) {

            case 0:
                wordStartID = 0;
                wordEndID = Word.idWordBase.get("GRE threek Words") - 1;
                break;
            case 1:
                wordStartID = Word.idWordBase.get("GRE threek Words");
                wordEndID = Word.idWordBase.get("TOEFL") - 1;
                break;
            case 2:
                wordStartID = Word.idWordBase.get("TOEFL");
                wordEndID = Word.idWordBase.get("IETLS") - 1;
                break;
        }
        db = new MySQLiteHandler(getApplicationContext());
        HashMap<String, String> userDetails = db.getUserDetails();
        currentUserID = Integer.parseInt(userDetails.get("userID"));
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.mipmap.bg);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        for (int i = 0; i < titles.length; i++) {
            item[i] = new ResideMenuItem(this, icon[i], titles[i]);
            item[i].setOnClickListener(this);
            resideMenu.addMenuItem(item[i], ResideMenu.DIRECTION_LEFT);
        }
        if (savedInstanceState == null)
            changeFragment(new HomeFragment());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.title_bar_right_menu:
                changeFragment(new FavouriteWordFragment());
                break;

        }
        if (v == item[0]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("Vocabulary Assistant");
            changeFragment(new HomeFragment());
            resideMenu.closeMenu();
        }
        if (v == item[1]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("My Notebook");
            changeFragment(new MyNoteBookFragment());
            resideMenu.closeMenu();
        }
        if (v == item[2]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("Learning Trace");
            changeFragment(new LearningTraceFragment());
            resideMenu.closeMenu();
        }
        if (v == item[3]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("Message");
            changeFragment(new SettingFragment());
            resideMenu.closeMenu();
        }
        if (v == item[4]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("Setting");
            changeFragment(new SettingFragment());
            resideMenu.closeMenu();
        }
        if (v == item[5]) {
            TextView textView = (TextView) findViewById(R.id.title_main);
            textView.setText("Setting");
            changeFragment(new SettingFragment());
            resideMenu.closeMenu();
        }

    }

    @Override
    public void onBackPressed() {
        // Disable going back to the LoginActivity
        moveTaskToBack(true);


    }


    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
