package com.limi.andorid.vocabularyassistant.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Word;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.WordImportHandler;

import java.util.ArrayList;
import java.util.Collections;

public class SpellingReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private int correctID;
    private int startID;
    private int endID;
    private int length;
    private int currentID;
    private TextView meaningView;
    private TextView hintView;
    private EditText editText;
    private Button showAnswer;
    private Button nextButton;
    private ArrayList<Word> testWords;
    private boolean isWrong = false;

    private MySQLiteHandler mySQLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spelling_review);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        startID = bundle.getInt("StartID");
        endID = bundle.getInt("EndID");

        length = endID - startID + 1;

        testWords = new ArrayList<Word>();

        for (int i = startID; i <= endID; i++) {
            testWords.add(WordImportHandler.systemWordBaseArrayList.get(i));
        }
        Collections.shuffle(testWords);


        currentID = 0;

        meaningView = (TextView) findViewById(R.id.meaningSpell);
        hintView = (TextView) findViewById(R.id.spellHint);
        editText = (EditText) findViewById(R.id.spellText);
        nextButton = (Button) findViewById(R.id.next_button_r);
        showAnswer = (Button) findViewById(R.id.spellShowAns);


        assert nextButton != null;
        nextButton.setOnClickListener(this);
        showAnswer.setOnClickListener(this);

        mySQLiteHandler = new MySQLiteHandler(getApplicationContext());

        updateView();
    }

    private void updateView() {

        isWrong = false;
        nextButton.setVisibility(View.INVISIBLE);


        Word currentWord = testWords.get(currentID);
        String meaning = currentWord.getTrans();
        String showMeaning = "";
        if (meaning.contains("\n")) {
            String[] meanings = meaning.split("\n");
            if (meanings[0].contains("1")) {
                showMeaning = meanings[0].substring(3);
            }
        } else {
            if (meaning.contains("1")) {
                showMeaning = meaning.substring(3);
            } else {
                showMeaning = meaning;
            }
        }
        meaningView.setText(showMeaning);
        int wordLength = currentWord.getWord().length();
        String hintText = "Word length is " + wordLength;
        hintView.setText(hintText);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button_r:
                //show answer
                break;
            case R.id.spellShowAns:

                break;


        }
    }
}
