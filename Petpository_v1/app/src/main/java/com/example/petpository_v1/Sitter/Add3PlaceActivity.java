package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Add3PlaceActivity extends AppCompatActivity {

    private Button saveBt, backtwoBt;
    private EditText Tel, Web, Fb, Line, Ig;
    private Place place;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String uId;
    private String keyGen;
    private Intent intent, intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add3_place);


        intent = getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        place = (Place) intent.getSerializableExtra("numpricedaysize");
        keyGen = intent.getStringExtra("KeyGen");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uId = mFirebaseUser.getUid();

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
                place.setPlaceId(keyGen);
                place.setUidSitter(uId);

                mDatabase.child("Sitter").child(keyGen).setValue(place);

                intent1 = new Intent(Add3PlaceActivity.this, SitterMainActivity.class);
                finish();
                startActivity(intent1);
            }
        });

        backtwoBt = (Button)findViewById(R.id.backtotwo);
        backtwoBt.setText("< BACK");
        backtwoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}