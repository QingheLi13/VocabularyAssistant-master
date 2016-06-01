package com.limi.andorid.vocabularyassistant.acti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Task;
import com.limi.andorid.vocabularyassistant.data.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by limi on 16/5/10.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private Context mContext;

    public TaskAdapter(Context context, ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
        this.mContext = context;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mission_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        Collections.sort(taskArrayList, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
        final Task task = taskArrayList.get(position);
        holder.listTextView.setText("Anytime reciting task " + position);
        String bookTitle = "";
        switch (task.getBookId()) {
            case 0:
                bookTitle += "GRE";
                break;
            case 1:
                bookTitle += "TOEFL";
                break;
            case 2:
                bookTitle += "IETLS";
                break;
        }
        holder.bookTextView.setText(bookTitle);
        String listText = "List: " + task.getStartList() + " Unit: " + task.getStartUnit() + ",List: " + task.getEndList() + " Unit: " + task.getEndUnit();
        holder.listTextView.setText(listText);
        holder.buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskArrayList.remove(position);
                notifyDataSetChanged();

            }
        });
        holder.buttonStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int baseID = 0;
                switch (task.getBookId()) {
                    case 0:
                        baseID = 0;
                        break;
                    case 1:
                        baseID = Word.idWordBase.get("GRE threek Words");
                        break;
                    case 2:
                        baseID = Word.idWordBase.get("TOEFL");
                        break;
                }
                int startID = baseID + (task.getStartList() - 1) * 100 + (task.getStartUnit() - 1) * 10;
                SummaryActivity.finishID = baseID + (task.getEndList() - 1) * 100 + (task.getEndUnit()) * 10 - 1;
                Intent intent = new Intent(mContext, RecitingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("nextStartID", startID);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return taskArrayList == null ? 0 : taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView anytimeRecite;
        public TextView bookTextView;
        public TextView listTextView;
        public Button buttonStr;
        public Button buttonDel;

        public ViewHolder(View itemView) {
            super(itemView);
            anytimeRecite = (TextView) itemView.findViewById(R.id.anytime_reciting_list);
            bookTextView = (TextView) itemView.findViewById(R.id.bookset);
            listTextView = (TextView) itemView.findViewById(R.id.listset);
            buttonStr = (Button) itemView.findViewById(R.id.start_btn);
            buttonDel = (Button) itemView.findViewById(R.id.delete_btn);

        }
    }
}
