package com.example.petpository_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Owner.AddPet1Activity;
import com.example.petpository_v1.Owner.MyPetsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String mode;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private String mEmail;
    private String mPhotoUrl;

    TextView mainButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*** Get mode from shared preference **/
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        mode = sharedPreferences.getString("mode", null);

        /** Get User data**/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUsername = mFirebaseUser.getDisplayName();
        mEmail = mFirebaseUser.getEmail();

        TextView username = (TextView) findViewById(R.id.user_name);
        TextView userMail = (TextView) findViewById(R.id.user_email);
        ImageView image = (ImageView) findViewById(R.id.user_image);
        username.setText(mUsername);
        userMail.setText(mEmail);

        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            Glide.with(this)
                    .load(mPhotoUrl)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(image);
        }

        mainButton = (TextView) findViewById(R.id.main_button);


        if (mode.equals("Owner")) {
            mainButton.setText("My pets");
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ProfileActivity.this, "toastttt", Toast.LENGTH_SHORT).show();
                    Intent intentToMyPets = new Intent(ProfileActivity.this, MyPetsActivity.class);
                    startActivity(intentToMyPets);
//                    finish();
                }
            });
        }else if (mode.equals("Sitter")){
            mainButton.setText("My places");
        }

    }

    public void switchMode(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("mode");
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
    }

}
