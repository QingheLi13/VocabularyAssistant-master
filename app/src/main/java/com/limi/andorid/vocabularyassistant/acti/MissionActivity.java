package com.limi.andorid.vocabularyassistant.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.Task;

/**
 * Created by limi on 16/5/10.
 */
public class MissionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskAdapter myAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        Button add_btn = (Button) findViewById(R.id.add_btn);
        assert add_btn != null;
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddingTaskActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.anytime_reciting_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setHasFixedSize(true);

        myAdapter = new TaskAdapter(this, Task.tasks);

        mRecyclerView.setAdapter(myAdapter);

    }

}
