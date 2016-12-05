package com.example.petpository_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.petpository_v1.Owner.OwnerMainActivity;
import com.example.petpository_v1.Sitter.SitterMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, SitterMainActivity.class);
        startActivity(intent);
    }
}
