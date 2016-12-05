package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.RecentRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentRequestActivity extends AppCompatActivity {

    RecentRequestAdapter adapter;
    ArrayList<RequestPet> requests;
    String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_request);

        Intent intent = getIntent();
        placeId = intent.getStringExtra("placeId");

        getAllRequest();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.requestRecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new RecentRequestAdapter(getApplicationContext(), requests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getAllRequest(){
        requests = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("RequestPet").orderByChild("requestPlaceID").equalTo(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()){
                    RequestPet request = requestSnapshot.getValue(RequestPet.class);
                    Log.d("requet Pet>>>>>>>>>", request.getRequestID());
                    requests.add(request);
                }
                filterOnlyAcceptRequest();
                Log.d("aaaa", requests.size() + "");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void filterOnlyAcceptRequest(){
        for (RequestPet requestPet: requests){
            if(!requestPet.getRequestStatus().equals("accept")){
                Log.d("delete request", requestPet.getRequestID());
                requests.remove(requestPet);
            }
        }
    }
}
