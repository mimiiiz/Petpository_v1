package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.petpository_v1.R;

public class SitterMainActivity extends AppCompatActivity {

    private Button btn_gotoPending;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_main);

        //goto pending ... mimi
        btn_gotoPending = (Button) findViewById(R.id.btn_gotoPending);
        btn_gotoPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoPending = new Intent(SitterMainActivity.this, PendingActivity.class);
                placeId = "123";
                gotoPending.putExtra("placeId", placeId);
                startActivity(gotoPending);
            }
        });

    }
}
