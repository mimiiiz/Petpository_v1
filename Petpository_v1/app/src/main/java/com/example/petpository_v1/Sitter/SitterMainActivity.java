package com.example.petpository_v1.Sitter;

import com.google.firebase.database.DatabaseError;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.petpository_v1.ProfileActivity;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.MyPlaceAdapterRecycler;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SitterMainActivity extends AppCompatActivity {


    ImageView placeImageView;
    TextView placeNameLabel;
    MyPlaceAdapterRecycler myPlaceAdapterRecycler;
    ArrayList<Place> places;
    RecyclerView recyclerView;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_main);

        setTitle("My Places");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uId = user.getUid();
            Log.d("Uiddddd>>>>>>>>>>>>>>", uId);
        }

        getMyPlaces();

        placeImageView = (ImageView) findViewById(R.id.placeImageViewer);
        placeNameLabel = (TextView) findViewById(R.id.placeDogNumberLabel);

        recyclerView = (RecyclerView) findViewById(R.id.sitterMainRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        myPlaceAdapterRecycler = new MyPlaceAdapterRecycler(getApplicationContext(), places);
        recyclerView.setAdapter(myPlaceAdapterRecycler);


    }

    private void getMyPlaces() {
        places = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Query query = databaseReference.child("Sitter").orderByChild("uidSitter").equalTo(uId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                    Log.d("ddddddd", dataSnapshot.toString());
                    Place place = placeSnapshot.getValue(Place.class);
                    place.setPlaceId(placeSnapshot.getKey());
                    places.add(place);
                }
                myPlaceAdapterRecycler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }




    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

}
