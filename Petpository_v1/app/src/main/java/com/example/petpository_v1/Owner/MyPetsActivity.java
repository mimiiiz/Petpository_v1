package com.example.petpository_v1.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.petpository_v1.R;

public class MyPetsActivity extends AppCompatActivity {

    private Button btn_gotoAddPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);
        setTitle("My Pets");

        btn_gotoAddPet = (Button) findViewById(R.id.btn_gotoAddPet);
        btn_gotoAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddPet = new Intent(MyPetsActivity.this, AddPet1Activity.class);
                startActivity(intentToAddPet);
            }
        });
    }
}
