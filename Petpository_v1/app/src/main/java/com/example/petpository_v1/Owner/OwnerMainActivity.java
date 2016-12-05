package com.example.petpository_v1.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.petpository_v1.ProfileActivity;
import com.example.petpository_v1.R;

public class OwnerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        setTitle("Petpository");
    }

    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
