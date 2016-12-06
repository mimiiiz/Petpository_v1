package com.example.petpository_v1.Owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.PetListRecycleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoosePetActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference databaseReference;
    DatabaseReference myPetsRef;
    ArrayList<Pet> myPetList;
    String currentUserUID;
    ValueEventListener petListEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pet);

        Intent oldIntent = getIntent();
        Place place_detail = (Place) oldIntent.getSerializableExtra("place");

        SharedPreferences sp = getSharedPreferences("current_place", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        editor.putString("place_id", place_detail.getPlaceId());
        editor.putString("place_name", place_detail.getPlaceName());
        editor.putString("owner_id", currentUserUID);
        editor.putString("sitter_id",place_detail.getUidSitter());
        editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        myPetList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.pet_list_recyle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myPetsRef = databaseReference.child("Owner")
                .child(currentUserUID).child("Pet");
        myPetsRef.limitToFirst(30);
        petListEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myPetList.clear();
                for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()){
                    Pet pet = dtSnapshot.getValue(Pet.class);
                    myPetList.add(pet);
                }
                if (myPetList.size() > 0){
                    mAdapter = new PetListRecycleAdapter(myPetList, ChoosePetActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    LinearLayout warningText = (LinearLayout) findViewById(R.id.no_pet_text);
                    TextView msg = (TextView) findViewById(R.id.choose_pet_text);
                    msg.setVisibility(View.INVISIBLE);
                    warningText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myPetsRef.addValueEventListener(petListEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (petListEventListener != null) {
            myPetsRef.removeEventListener(petListEventListener);
        }
        finish();
    }

    public void addPet(View view){
        startActivity(new Intent(this,AddPet1Activity.class));
        finish();
    }
}
