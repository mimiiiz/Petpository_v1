package com.example.petpository_v1.Sitter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.MainActivity;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.RecentRequestAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecentRequestActivity extends AppCompatActivity implements RecentRequestAdapter.ViewHolder.ClickListener{

    private RecentRequestAdapter adapter;
    private ArrayList<RequestPet> requests;
    private String placeId;
    private ProgressDialog mProgressDialog;
    private TextView noCardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_request);
        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("All Accepted Request");

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        placeId = intent.getStringExtra("placeId");
        showProgressDialog();

        noCardTextView = (TextView) findViewById(R.id.noCardTextView);

        getAllRequest();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.requestRecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new RecentRequestAdapter(getApplicationContext(), requests, RecentRequestActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    private void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    private void getAllRequest(){
        requests = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("RequestPet").orderByChild("requestPlaceID").equalTo(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()){
                    RequestPet request = requestSnapshot.getValue(RequestPet.class);
                    if(request.getRequestStatus().equals("accept")){
                        requests.add(request);
                    }
                }
                Log.d("aaaa", requests.size() + "");
                adapter.notifyDataSetChanged();
                hideProgressDialog();
                if(requests.size() == 0){
                    noCardTextView.setText("ไม่มีการร้องขอ");
                    noCardTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(final int position) {
        Log.d("aaaa", "aaaaa");
        final Dialog dialog = new Dialog(RecentRequestActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recentpet_dialog);

        final ImageView dogImg = (ImageView) dialog.findViewById(R.id.dogimg);

        TextView nametv = (TextView) dialog.findViewById(R.id.nametv) ;
        nametv.setText(requests.get(position).getPet().getPetName());
        TextView sizetv = (TextView) dialog.findViewById(R.id.sizetv) ;
        sizetv.setText(requests.get(position).getPet().getPetSize());
        TextView breedtv = (TextView) dialog.findViewById(R.id.breedtv) ;
        breedtv.setText(requests.get(position).getPet().getPetType());
        final TextView ownertv = (TextView) dialog.findViewById(R.id.ownertv) ;
        TextView teltv = (TextView) dialog.findViewById(R.id.teltv) ;
        TextView startDate = (TextView) dialog.findViewById(R.id.datetv);
        startDate.setText(requests.get(position).getRequestStartDate());
        TextView to = (TextView) dialog.findViewById(R.id.to);
        TextView toDate = (TextView) dialog.findViewById(R.id.totv);
        teltv.setText(requests.get(position).getOwnerPhoneNo());
        teltv.setSelected(true);
        ImageButton telOwner = (ImageButton)dialog.findViewById(R.id.telowner);
        ImageButton closebt = (ImageButton)dialog.findViewById(R.id.closebt);
        closebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if(requests.get(position).getRequestStartDate().equals(requests.get(position).getRequestEndDate())){
            to.setVisibility(View.GONE);
            toDate.setVisibility(View.GONE);
        }else{
            to.setVisibility(View.VISIBLE);
            toDate .setText(requests.get(position).getRequestEndDate());
        }
        telOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTelOwner(v, requests.get(position).getOwnerPhoneNo());
            }
        });


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(requests.get(position).getRequestUID_owner()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ownerName = (String) dataSnapshot.getValue();
                ownertv.setText(ownerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        StorageReference dogImageStorage = storageRef.child("Owner/" + requests.get(position).getRequestUID_owner() + "/" + requests.get(position).getPet().getPetID() + "/0");

        dogImageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).fitCenter().centerCrop().into(dogImg);
            }
        });


        dialog.show();
    }

    public void onClickTelOwner(View v, String phoneNo){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        if (ActivityCompat.checkSelfPermission(RecentRequestActivity.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RecentRequestActivity.this,
                    android.Manifest.permission.CALL_PHONE)) {
            }
        }
        startActivity(callIntent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
