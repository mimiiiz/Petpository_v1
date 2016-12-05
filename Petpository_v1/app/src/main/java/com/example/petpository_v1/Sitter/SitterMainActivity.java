package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.example.petpository_v1.ProfileActivity;
import com.example.petpository_v1.R;

public class SitterMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_main);
        setTitle("My place");
    }

    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

}
