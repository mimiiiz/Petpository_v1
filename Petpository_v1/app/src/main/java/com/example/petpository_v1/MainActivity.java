package com.example.petpository_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.petpository_v1.Sitter.SitterMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Petpository");

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
    }

    public void gotoSitterMain(View view){
        Intent gotoSitterMain = new Intent(MainActivity.this, SitterMainActivity.class);
        startActivity(gotoSitterMain);
    }

    public void gotoSitterMain(View view){
        Intent gotoSitterMain = new Intent(MainActivity.this, SitterMainActivity.class);
        startActivity(gotoSitterMain);
    }

    public void gotoSitterMain(View view){
        Intent gotoSitterMain = new Intent(MainActivity.this, SitterMainActivity.class);
        startActivity(gotoSitterMain);
    }
}
