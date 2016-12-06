package com.example.petpository_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpository_v1.Model.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Add3PlaceActivity extends AppCompatActivity {

    Button saveBt, backtwoBt;
    EditText Tel, Web, Fb, Line, Ig;
    Place place;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add3_place);

        Intent intent = getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        place = (Place) intent.getSerializableExtra("numpricedaysize");

        saveBt = (Button)findViewById(R.id.saveBt);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tel = (EditText)findViewById(R.id.telnum);
                Web = (EditText)findViewById(R.id.web);
                Fb = (EditText)findViewById(R.id.fb);
                Line = (EditText)findViewById(R.id.line);
                Ig = (EditText)findViewById(R.id.insta);
                Toast.makeText(getApplicationContext(),"add notice complete", Toast.LENGTH_SHORT).show();

                place.setPlaceTel(Tel.getText().toString());
                place.setPlaceWebsite(Web.getText().toString());
                place.setPlaceFacebook(Fb.getText().toString());
                place.setPlaceLine(Line.getText().toString());
                place.setPlaceIg(Ig.getText().toString());

                String keyGen = mDatabase.push().getKey();
                mDatabase.child("Place").child(keyGen).setValue(place);
            }
        });

        backtwoBt = (Button)findViewById(R.id.backtotwo);
        backtwoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}