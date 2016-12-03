package com.example.petpository_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_gotoAddPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add pet ... mimi
        btn_gotoAddPet = (Button) findViewById(R.id.btn_gotoAddPet);
        btn_gotoAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAddPet = new Intent(MainActivity.this, AddPetActivity.class);
                startActivity(gotoAddPet);
            }
        });

    }
}
