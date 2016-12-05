package com.example.petpository_v1.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.ProfileActivity;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.PlaceRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class OwnerMainActivity extends AppCompatActivity {

    private List<Place> placeList;
    private List<String> fileName;
    private RecyclerView recyclerView;
    private PlaceRecyclerAdapter placeAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        setTitle("All Places");

        getPlaceList();

        Log.d("PlaceCardListActivity", "Hi");


        recyclerView = (RecyclerView) findViewById(R.id.placeRecyClerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Log.d("d;lk;lsdkfl;ksdlf", placeList.size() + "");

        placeAdapter = new PlaceRecyclerAdapter(OwnerMainActivity.this, placeList);
        recyclerView.setAdapter(placeAdapter);

    }

    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void getPlaceList(){

        Log.d("getPlacedList", "HI");

        placeList = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

//        Query recentPlaceQuery = myRef.child("Place").limitToLast(25);
        Query recentPlaceQuery = myRef.child("Sitter").limitToLast(25);
        recentPlaceQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot placeSnapshot: dataSnapshot.getChildren()){
                    Place place = new Place();
                    place = placeSnapshot.getValue(Place.class);
                    Log.d("key", placeSnapshot.toString());
                    place.setPlaceId(placeSnapshot.getKey());
//                    Log.d("DDD", ">>>>>>>>>>>>>>>>>>"+placeSnapshot.getKey());
//                    fileName.add(placeSnapshot.getKey());
                    placeList.add(place);
                    Log.d("ppppppppppppppppppp", placeSnapshot.toString());
                }
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
