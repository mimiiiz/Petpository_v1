package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.petpository_v1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PendingActivity extends AppCompatActivity {

    private String requestPlaceId;
    private TextView tv_requestPetName, tv_requestOwnerEmail, tv_requestPetSize,
            tv_requestPetType, tv_requestStartDate, tv_requestEndDate ;
    private Button btn_accept, btn_denied;
    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        setTitle("Pending Request");

        Intent intent = getIntent();
        requestPlaceId = intent.getStringExtra("placeId");




    }
}
