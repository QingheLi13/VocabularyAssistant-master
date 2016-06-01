package com.limi.andorid.vocabularyassistant.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.UserWord;
import com.limi.andorid.vocabularyassistant.data.Word;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.WordImportHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MeaningReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private int correctID;
    private int startID;
    private int endID;
    private int length;
    private int currentID;
    private TextView meaningView;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    //    private int[] randomArray = new int[4];
    private HashSet<Integer> randomSet;
    private ArrayList<Integer> choice;
    private Button nextButton;
    private ArrayList<Word> testWords;
    private boolean isWrong = false;

    private MySQLiteHandler mySQLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning_review);

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

        meaningView = (TextView) findViewById(R.id.meaningWord);

        button1 = (Button) findViewById(R.id.meaning1);
        button2 = (Button) findViewById(R.id.meaning2);
        button3 = (Button) findViewById(R.id.meaning3);
        button4 = (Button) findViewById(R.id.meaning4);
        button5 = (Button) findViewById(R.id.meaning5);
        nextButton = (Button) findViewById(R.id.next_button_m);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

        nextButton.setOnClickListener(this);

        mySQLiteHandler = new MySQLiteHandler(getApplicationContext());

        updateView();

    }

    private void updateView() {
        isWrong = false;
        nextButton.setVisibility(View.INVISIBLE);
        randomSet = new HashSet<>();
        randomSet.add(currentID);
        getRandomNumber();
        choice = new ArrayList<>(randomSet);

//        choice.add(testWords.get(currentID).getId());

        Collections.shuffle(choice);
        for (int i = 0; i < choice.size(); i++) {
            if (choice.get(i) == currentID) {
                correctID = i + 1;
            }
        }

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


        button1.setText(testWords.get(choice.get(0)).getWord());
        button2.setText(testWords.get(choice.get(1)).getWord());
        button3.setText(testWords.get(choice.get(2)).getWord());
        button4.setText(testWords.get(choice.get(3)).getWord());

        button1.setBackgroundDrawable(getResources().getDrawable(R.mipmap.review_btn2));
        button2.setBackgroundDrawable(getResources().getDrawable(R.mipmap.review_btn2));
        button3.setBackgroundDrawable(getResources().getDrawable(R.mipmap.review_btn2));
        button4.setBackgroundDrawable(getResources().getDrawable(R.mipmap.review_btn2));
        button5.setBackgroundDrawable(getResources().getDrawable(R.mipmap.review_btn2));
    }

    private void getRandomNumber() {
        while (randomSet.size() < 4) {
            int randomNumber = (int) Math.round(Math.random() * length - 1);
            if (randomNumber < length && randomNumber >= 0) {
                randomSet.add(randomNumber);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meaning1:
                if (correctID == 1) {
                    setCorrectButton(button1);
                } else {
                    isWrong = true;
                    setWrongButton(button1);
                }
                break;
            case R.id.meaning2:
                if (correctID == 2) {
                    setCorrectButton(button2);
                } else {
                    isWrong = true;
                    setWrongButton(button2);
                }
                break;
            case R.id.meaning3:
                if (correctID == 3) {
                    setCorrectButton(button3);
                } else {
                    isWrong = true;
                    setWrongButton(button3);
                }
                break;
            case R.id.meaning4:
                if (correctID == 4) {
                    setCorrectButton(button4);
                } else {
                    isWrong = true;
                    setWrongButton(button4);
                }
                break;
            case R.id.meaning5:
                isWrong = true;
                button5.setBackgroundDrawable(getResources().getDrawable(R.mipmap.meaning_nknow1));
                break;
            case R.id.next_button_m:
                showNextWordView();
                break;
        }

    }

    private void setCorrectButton(Button button) {
        button.setBackgroundDrawable(getResources().getDrawable(R.mipmap.meaning_correct2));
        nextButton.setVisibility(View.VISIBLE);
    }

    private void setWrongButton(Button button) {
        button.setBackgroundDrawable(getResources().getDrawable(R.mipmap.meaning_wrong2));

    }

    private void showNextWordView() {
//        if(currentID)
        if (isWrong) {
            UserWord.userWordHashMap.get(testWords.get(correctID).getId()).inWrongTime();
            mySQLiteHandler.increaseWrongTime(UserWord.userWordHashMap.get(testWords.get(correctID).getId()));
        }
        if (currentID + startID < endID) {
            currentID++;
            updateView();
        } else {

            Intent intent2 = new Intent(MeaningReviewActivity.this, SummaryActivity.class);
            setResult(0, intent2);
            finish();
        }
    }
}
