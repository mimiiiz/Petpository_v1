package com.example.petpository_v1.Owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.Model.User;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.HistoryRecycleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<RequestPet> historyList;

    DatabaseReference reference;
    DatabaseReference requestRef;
    ValueEventListener listener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("History");

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.owner_history_recycle);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        historyList = new ArrayList();

        // specify an adapter (see also next example)
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference = FirebaseDatabase.getInstance().getReference();
        requestRef = reference.child("RequestPet");
        requestRef.limitToLast(50);
        user = FirebaseAuth.getInstance().getCurrentUser();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    RequestPet request = snapshot.getValue(RequestPet.class);
                    if(request.getRequestUID_owner().equals(user.getUid())){
                        historyList.add(request);
                    }
                }
                mAdapter = new HistoryRecycleAdapter(historyList, History.this, user.getUid());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        requestRef.addValueEventListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener!=null){
            requestRef.removeEventListener(listener);
        }
    }
}
