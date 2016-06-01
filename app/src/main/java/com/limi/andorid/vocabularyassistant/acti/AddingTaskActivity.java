package com.limi.andorid.vocabularyassistant.acti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Task;
import com.limi.andorid.vocabularyassistant.dialog.BookPicker;
import com.limi.andorid.vocabularyassistant.dialog.ListUnitPicker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddingTaskActivity extends AppCompatActivity {

    public static int currentBookValue;
    private int startListValue;
    private int startUnitValue;
    private ListUnitPickerDialog listUnitPickerDialog;
    private BookDialog bookDialog;
    private int endListValue;
    private int endUnitValue;
    private TextView[] textViews;

    private Button cancelBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_task);
        textViews = new TextView[3];
        ListView listView = (ListView) findViewById(R.id.task_list);
        List<Map<String, Object>> list = new ArrayList<>();
        String[] firstPart = {"Vocabulary Book: ", "Start position: ", "End position: "};
        String[] secondPart = {"Select the vocabulary book", "Select the start position", "Select the end position"};
        for (int i = 0; i < 3; i++) {
            Map<String, Object> stringObjectMap = new LinkedHashMap<>();
            stringObjectMap.put("first", firstPart[i]);
            stringObjectMap.put("second", secondPart[i]);
            list.add(stringObjectMap);
        }
        assert listView != null;
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this, list);
        listView.setAdapter(myListViewAdapter);
        listUnitPickerDialog = new ListUnitPickerDialog(this, startListValue, startUnitValue);
        bookDialog = new BookDialog(this, currentBookValue);

        cancelBtn = (Button) findViewById(R.id.undo_button);
        addBtn = (Button) findViewById(R.id.finish_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task(startListValue, endListValue, startUnitValue, endUnitValue, currentBookValue);
                Intent intent = new Intent(getApplicationContext(), MissionActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void showDialog(String s) {
        listUnitPickerDialog.setValue(Integer.parseInt(s));
        listUnitPickerDialog.show();

        startListValue = listUnitPickerDialog.getListValue();
        startUnitValue = listUnitPickerDialog.getUnitValue();
    }

    public void showBookDialog() {
        bookDialog.show();
        currentBookValue = bookDialog.getBookValue();

    }

    class MyListViewAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Map<String, Object>> list;
        private Context context;

        public MyListViewAdapter(Context context, List<Map<String, Object>> data) {
            this.context = context;
            this.list = data;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView tvFirst_part = null;
            TextView tvSecond_part = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_layout, null);
                tvFirst_part = (TextView) convertView.findViewById(R.id.firstPart);
                tvSecond_part = (TextView) convertView.findViewById(R.id.secondPart);
            } else {
                tvFirst_part = (TextView) convertView.getTag();
                tvSecond_part = (TextView) convertView.getTag();
            }

            tvFirst_part.setText((String) list.get(position).get("first"));
            tvSecond_part.setText((String) list.get(position).get("second"));


//            final TextView finalTvSecond_part = tvSecond_part;
            final TextView finalTvSecond_part = tvSecond_part;
            tvSecond_part.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            textViews[0] = finalTvSecond_part;
                            showBookDialog();
                            break;
                        case 1:
                            textViews[1] = finalTvSecond_part;
                            showDialog("1");
//                            finalTvSecond_part.setText("List:" + startListValue + ", Unit: " + startUnitValue);
                            break;
                        case 2:
                            textViews[2] = finalTvSecond_part;
                            showDialog("2");
//                            showDialog1();
//                            finalTvSecond_part.setText("List:" + endListValue + ", Unit: " + endUnitValue);
                            break;
                    }
                }
            });

            return convertView;

        }


    }

    class ListUnitPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

        private ListUnitPicker listUnitPicker;
        private int listValue = 1;
        private int unitValue = 1;
        private int position;

        public ListUnitPickerDialog(Context context, int list1, int unit1) {
            super(context);

            listUnitPicker = new ListUnitPicker(context, listValue, unitValue);
            setView(listUnitPicker);
            listUnitPicker.setOnDataChangerListener(new ListUnitPicker.onValueChangeListener() {
                @Override
                public void onDataChange(ListUnitPicker view, int list, int unit) {
                    listValue = list;
                    unitValue = unit;
                    Log.d("list", String.valueOf(list));
                    Log.d("unit", String.valueOf(unit));
                }
            });
            setButton(BUTTON_POSITIVE, "SET", this);
            setButton(BUTTON_NEGATIVE, "CANCEL", (OnClickListener) null);

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (position) {
                case 1:
                    textViews[position].setText("List:" + listValue + ", Unit: " + unitValue);
                    startListValue = listValue;
                    startUnitValue = unitValue;
                    break;
                case 2:
                    textViews[position].setText("List:" + listValue + ", Unit: " + unitValue);
                    endListValue = listValue;
                    endUnitValue = unitValue;
                    break;
            }

        }

        public int getListValue() {
            return listValue;
        }

        public int getUnitValue() {
            return unitValue;
        }

        public void setValue(int value) {
            this.position = value;
        }

        public void setListUnitPicker(int book) {
            listUnitPicker.bookSelector(book);
        }
    }

    class BookDialog extends AlertDialog implements DialogInterface.OnClickListener {

        private BookPicker bookPicker;
        private int bookValue = 0;

        public BookDialog(Context context, int book) {
            super(context);

            bookPicker = new BookPicker(context, book);
            setView(bookPicker);
            bookPicker.setOnDataChangerListener(new BookPicker.onValueChangeListener() {
                @Override
                public void onDataChange(BookPicker view, int book) {
                    bookValue = BookPicker.book;
                }

            });
            setButton(BUTTON_POSITIVE, "SET", this);
            setButton(BUTTON_NEGATIVE, "CANCEL", (OnClickListener) null);

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (bookValue) {
                case 0:
                    textViews[0].setText("GRE");
                    break;
                case 1:
                    textViews[0].setText("TOEFL");
                    break;
                case 2:
                    textViews[0].setText("IETLS");
                    break;

            }
            listUnitPickerDialog.setListUnitPicker(bookValue);
            currentBookValue = bookValue;

        }

        public int getBookValue() {
            return bookValue;
        }
    }
}
