package com.example.petpository_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.petpository_v1.Owner.OwnerMainActivity;
import com.example.petpository_v1.Sitter.SitterMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void enterAsOwner(View view){
        editor.putString("mode", "Owner");
        editor.commit();
        startActivity(new Intent(this, OwnerMainActivity.class));
        finish();
    }

    public void enterAsSitter(View view){
        editor.putString("mode", "Sitter");
        editor.commit();
        startActivity(new Intent(this, SitterMainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mode = sharedPreferences.getString("mode",null);
        if (mode != null) {
            if (mode.equals("Owner")) {
                startActivity(new Intent(this, OwnerMainActivity.class));
                finish();
            } else if (mode.equals("Sitter")) {
                startActivity(new Intent(this, SitterMainActivity.class));
                finish();
            }
        }
    }
}
