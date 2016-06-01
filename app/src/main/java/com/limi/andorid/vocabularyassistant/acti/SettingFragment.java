package com.limi.andorid.vocabularyassistant.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.UserWord;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.SessionManager;

import java.util.ArrayList;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private static SettingFragment instance;
    private View parentView;
    private ListView list;
    private MySQLiteHandler db;
    private SessionManager session;

    public static SettingFragment getInstance() {
        if (instance == null) {
            synchronized (SettingFragment.class) {
                if (instance == null) instance = new SettingFragment();
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_setting, container, false);
        list = (ListView) parentView.findViewById(R.id.listView);
        Button logout_button = (Button) parentView.findViewById(R.id.btn_logout);
        logout_button.setOnClickListener(this);
        db = new MySQLiteHandler(getActivity());

        // session manager
        session = new SessionManager(getActivity());
        initView();


        return parentView;
    }

    private void initView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getCalendarData());
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();

            }
        });
    }

    private ArrayList<String> getCalendarData() {
        ArrayList<String> wordList = new ArrayList<>();
        wordList.add("UserAccount Information");
        wordList.add("Vocabulary Selection");
        wordList.add("Reset Planning");
        wordList.add("Notification");
        wordList.add("Help");
        return wordList;
    }

    private void logoutUser() {
        session.setLogin(false);
        session.setKeyIsSelected(false);

        db.deleteUsers();
//        db.deleteUserWord();
        UserWord.userWordHashMap.clear();
        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                logoutUser();
                break;
        }
    }
}
