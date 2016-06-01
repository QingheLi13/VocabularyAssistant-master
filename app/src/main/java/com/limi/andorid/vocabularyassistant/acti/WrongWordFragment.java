package com.limi.andorid.vocabularyassistant.acti;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.UserWord;
import com.limi.andorid.vocabularyassistant.data.Word;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.WordImportHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class WrongWordFragment extends Fragment {
    public static ArrayList<Integer> wordWrong = new ArrayList<>();
    private static WrongWordFragment instance;
    private View parentView;
    private ListView listView;

    private MySQLiteHandler db;

    public static WrongWordFragment getInstance() {
        if (instance == null) {
            synchronized (WrongWordFragment.class) {
                if (instance == null) instance = new WrongWordFragment();
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView != null) {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(parentView);
            }
            return parentView;
        }
        db = new MySQLiteHandler(getActivity());
        parentView = inflater.inflate(R.layout.notebook_fragment, container, false);
        listView = (ListView) parentView.findViewById(R.id.listView1);
        initView();
        return parentView;
    }

    private void initView() {
        final ArrayAdapter<Word> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getData());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = String.valueOf(arrayAdapter.getItem(i).getId());

//                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(parentView.getContext(), ViewFavWord.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", s);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

            }
        });
    }

    private ArrayList<Word> getData() {
        ArrayList<Word> wordList = new ArrayList<>();
        ArrayList<Word> words = WordImportHandler.systemWordBaseArrayList;
        if (UserWord.userWordHashMap.size() == 0) {
            ArrayList<UserWord> words1 = db.getUserWordData(MainActivity.currentUserID);
            for (UserWord word : words1) {
                UserWord.userWordHashMap.put(word.getWordID(), word);
            }
        }
        Iterator iterator = UserWord.userWordHashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, UserWord> mapEntry = (Map.Entry) iterator.next();
            Integer key = mapEntry.getKey();
            UserWord userWord = mapEntry.getValue();
            if (userWord.getWrongTime() >= 1 && userWord.getUserID() == MainActivity.currentUserID) {
                Word word = words.get((userWord.getWordID()));
                wordList.add(word);
                wordWrong.add(word.getId());
            }
        }
        return wordList;
    }

}
