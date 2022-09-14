package com.example.orcas4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyEventListActivity extends AppCompatActivity {
    private FloatingActionButton viewEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_list);

        viewEvent = findViewById(R.id.view_eventbtn);


        viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEventListActivity.this,UpdateEvent.class);
                startActivity(intent);

            }
        });
    }
}