package com.example.petpository_v1.Sitter;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
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

    private ImageView placeImageView;
    private TextView placeNameLabel;
    private MyPlaceAdapterRecycler myPlaceAdapterRecycler;
    private ArrayList<Place> places;
    private RecyclerView recyclerView;
    private String uId;

    int PLACE_PICKER_REQUEST = 1;
    Double placeLatitude, placeLongtitude;
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
                places.clear();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place placePick = PlacePicker.getPlace(data, this);
                String placeAdress = String.format("%s", placePick.getAddress());
                placeLatitude = Double.parseDouble(String.format("%f",placePick.getLatLng().latitude));
                placeLongtitude = Double.parseDouble(String.format("%f",placePick.getLatLng().longitude));
                Log.d("ddd",placeLatitude.toString()+placeLongtitude.toString());
                Intent intent = new Intent(SitterMainActivity.this, Add1PlaceActivity.class);
                intent.putExtra("placeaddress", placeAdress);
                intent.putExtra("lat", placeLatitude);
                intent.putExtra("long", placeLongtitude);
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void addPlaceBt(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;

        try {
            intent = builder.build(SitterMainActivity.this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST );
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
