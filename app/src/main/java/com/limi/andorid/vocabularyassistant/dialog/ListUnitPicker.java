package com.limi.andorid.vocabularyassistant.dialog;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Word;

/**
 * Created by limi on 16/5/8.
 */
public class ListUnitPicker extends FrameLayout {

    private final NumberPicker listSpinner;
    private final NumberPicker unitSpinner;
    private int minUnit = 1;
    private int maxUnit = 10;
    private int minList = 1;
    private int maxList;
    private int wordMaxID;
    private int list;

    private int unit;

    private onValueChangeListener mOnValueChangeListener;

    public ListUnitPicker(Context context, int listOld, int unitOld) {
        super(context);


        inflate(context, R.layout.two_number_picker_layout, this);

        int bookID = BookPicker.book;
        String bookTitle;
        switch (bookID) {
            case 0:
                wordMaxID = Word.idWordBase.get("GRE threek Words");
                Toast.makeText(context, String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();

                break;
            case 1:
                wordMaxID = Word.idWordBase.get("TOEFL") - Word.idWordBase.get("GRE threek Words");
                Toast.makeText(context, String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();
                break;
            case 2:
                wordMaxID = Word.idWordBase.get("IETLS") - Word.idWordBase.get("TOEFL");
                Toast.makeText(context, String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();

                break;
        }


        maxList = (wordMaxID + 1) / 100 + 1;
        listSpinner = (NumberPicker) this.findViewById(R.id.list);
        listSpinner.setMinValue(minList);
        listSpinner.setMaxValue(maxList);
        String[] sList = new String[maxList - minList + 1];
        for (int i = minList - 1; i < maxList; i++) {
            sList[i] = "List: " + (i + 1);
        }
        listSpinner.setDisplayedValues(sList);
        listSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        OnValueChangeListener mOnListChangedListener = new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    newVal = 1;
                }
                list = newVal;

                if (list == maxList) {
                    maxUnit = (wordMaxID - (maxList - 1) * 100) / 10 + 1;


                } else {
                    maxUnit = 10;
                }
                updateUnitControl();
                onDataChanged();

            }
        };
        listSpinner.setOnValueChangedListener(mOnListChangedListener);

        unitSpinner = (NumberPicker) this.findViewById(R.id.unit);
        unitSpinner.setMinValue(minUnit);
        unitSpinner.setMaxValue(maxUnit);
        unitSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        String[] sUnit = new String[maxUnit - minUnit + 1];
        for (int i = minUnit - 1; i < maxUnit; i++) {
            sUnit[i] = "Unit: " + (i + 1);
        }
        unitSpinner.setDisplayedValues(sUnit);
        OnValueChangeListener mOnUnitChangedListener = new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    newVal = 1;
                }
                unit = newVal;
                onDataChanged();

            }
        };
        unitSpinner.setOnValueChangedListener(mOnUnitChangedListener);

    }

    public void bookSelector(int bookID) {
        switch (bookID) {
            case 0:
                wordMaxID = Word.idWordBase.get("GRE threek Words");
                Toast.makeText(getContext(), String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();

                break;
            case 1:
                wordMaxID = Word.idWordBase.get("TOEFL") - Word.idWordBase.get("GRE threek Words");
                Toast.makeText(getContext(), String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();
                break;
            case 2:
                wordMaxID = Word.idWordBase.get("IETLS") - Word.idWordBase.get("TOEFL");
                Toast.makeText(getContext(), String.valueOf(wordMaxID), Toast.LENGTH_LONG).show();
                break;
        }
        maxList = (wordMaxID + 1) / 100 + 1;
        listSpinner.setDisplayedValues(null);
        listSpinner.setMinValue(minList);
        listSpinner.setMaxValue(maxList);
        String[] sList = new String[maxList - minList + 1];
        for (int i = minList - 1; i < maxList; i++) {
            sList[i] = "List: " + (i + 1);
        }
        listSpinner.setDisplayedValues(sList);
        listSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }

    private void updateUnitControl() {
        Log.d("MaxUnit", String.valueOf(maxUnit));
        unitSpinner.setDisplayedValues(null);
        String[] sUnit = new String[maxUnit - minUnit + 1];
        unitSpinner.setMinValue(minUnit);
        unitSpinner.setMaxValue(maxUnit);
        for (int i = minUnit - 1; i < maxUnit; i++) {
            sUnit[i] = "Unit: " + (i + 1);
        }
        unitSpinner.setDisplayedValues(sUnit);

    }

    public void setOnDataChangerListener(onValueChangeListener callback) {
        mOnValueChangeListener = callback;
    }

    private void onDataChanged() {
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onDataChange(this, list, unit);
        }
    }

    public interface onValueChangeListener {
        void onDataChange(ListUnitPicker view, int list, int unit);
    }
}
