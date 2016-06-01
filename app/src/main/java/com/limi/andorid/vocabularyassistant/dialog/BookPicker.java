package com.limi.andorid.vocabularyassistant.dialog;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.limi.andorid.vocabularyassistant.R;

/**
 * Created by limi on 16/5/9.
 */
public class BookPicker extends FrameLayout {

    public static int book;
    private NumberPicker bookSpinner;
    private onValueChangeListener mOnValueChangeListener;


    public BookPicker(final Context context, int listOld) {
        super(context);


        inflate(context, R.layout.book_picker_layout, this);

        bookSpinner = (NumberPicker) this.findViewById(R.id.book);
        bookSpinner.setMinValue(0);
        bookSpinner.setMaxValue(2);
        String[] sBook = {"GRE", "TOEFL", "IETLS"};
        bookSpinner.setDisplayedValues(sBook);
        bookSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        NumberPicker.OnValueChangeListener mOnListChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                book = newVal;
                onDataChanged();
                Toast.makeText(context, String.valueOf(book), Toast.LENGTH_LONG).show();

            }
        };
        bookSpinner.setOnValueChangedListener(mOnListChangedListener);

    }

    public void setOnDataChangerListener(onValueChangeListener callback) {
        mOnValueChangeListener = callback;
    }

    private void onDataChanged() {
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onDataChange(this, book);
        }
    }

    public interface onValueChangeListener {
        void onDataChange(BookPicker view, int book);
    }
}
