package com.example.orcas4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventInsertActivity extends AppCompatActivity {
    private Button backbtninsrt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_insert);

        backbtninsrt = findViewById(R.id.backbtn_insrtEvent);

        backbtninsrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInsertActivity.this,MyEventListActivity.class);
                startActivity(intent);
            }
        });
    }
}