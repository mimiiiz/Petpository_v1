package com.example.petpository_v1.Owner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.petpository_v1.Manifest;
import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.ProfileActivity;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.PlaceRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerMainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private List<Place> placeList;
    private List<String> fileName;
    private RecyclerView recyclerView;
    private PlaceRecyclerAdapter placeAdapter;
    private boolean sortLocation = false;
    private ImageButton sortCardBt;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        setTitle(null);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sortCardBt = (ImageButton) findViewById(R.id.sortCardBt);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)         //10 second, in millisecs
                .setFastestInterval(1 * 1000);   //1 second, millisecs

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
//            mGoogleApiClient.connect();
        }



        sortCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortLocation) {
                    sortLocation = true;
                    sortCardBt.setImageResource(R.drawable.ic_history_black_24dp);
//                    getPlaceList();
                    setDistance();
                    sortByLocation();
                } else {
                    sortLocation = false;
                    sortCardBt.setImageResource(R.drawable.ic_place_black_24dp);
                    getPlaceList();
                }
                placeAdapter.notifyDataSetChanged();
                Log.d("location", mLastLocation.toString());
            }
        });

        getPlaceList();

        recyclerView = (RecyclerView) findViewById(R.id.placeRecyClerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void profileMenu(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void getPlaceList(){

        placeList = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Query recentPlaceQuery = myRef.child("Sitter").limitToLast(25);
        recentPlaceQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Place> tempPlace = new ArrayList<Place>();
                for (DataSnapshot placeSnapshot: dataSnapshot.getChildren()){
                    Place place = placeSnapshot.getValue(Place.class);
                    place.setPlaceId(placeSnapshot.getKey());
                    placeList.add(place);
                }
                placeAdapter = new PlaceRecyclerAdapter(OwnerMainActivity.this, placeList);
                recyclerView.setAdapter(placeAdapter);

//                placeList.addAll(tempPlace);
                setDistance();
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            Log.d("discon", "discon");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(OwnerMainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation == null){
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else {
            handleNewLocation(mLastLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 999){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                onConnected(null);
            }
        }
    }

    private void handleNewLocation(Location location) {
        Log.d("mLastLocation", location.toString());
        setDistance();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
    mLastLocation = location;
        handleNewLocation(location);
    }

    private double calculateDistance(Location a, Location b){
        return a.distanceTo(b);
    }

    private void setDistance() {
        if (mLastLocation != null) {
            for (Place p: placeList) {
                Location l = new Location("aa");
                l.setLatitude(p.getPlaceLatitude());
                l.setLongitude(p.getPlaceLongtitude());
                p.setDistance(mLastLocation.distanceTo(l));
            }
        } else {
            onConnected(null);
        }
    }

    private void sortByLocation(){
        Collections.sort(placeList);
//        Collections.reverse(placeList);
    }
}
