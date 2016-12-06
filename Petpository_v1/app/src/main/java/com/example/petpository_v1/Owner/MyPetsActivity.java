package com.example.petpository_v1.Owner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.PetListRecycleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPetsActivity extends AppCompatActivity {

    FloatingActionButton btn_gotoAddPet;
    DatabaseReference databaseReference;
    DatabaseReference myPetsRef;
    ArrayList<Pet> myPetList;
    String currentUserUID;
    ValueEventListener petListEventListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);
        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("My Pets");

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_gotoAddPet = (FloatingActionButton) findViewById(R.id.btn_gotoAddPet);
        btn_gotoAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddPet = new Intent(MyPetsActivity.this, AddPet1Activity.class);
                startActivity(intentToAddPet);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        myPetList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.pet_list_recyle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,2);
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
                    mAdapter = new PetListRecycleAdapter(myPetList,MyPetsActivity.this);
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
}
