package com.example.petpository_v1.Sitter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PendingActivity extends AppCompatActivity {

    private String requestPlaceId;
    private TextView tv_requestPetName, tv_requestOwnerEmail, tv_requestPetSize,
            tv_requestPetType, tv_requestStartDate, tv_requestEndDate;
    protected DatabaseReference mDatabase;
    private RequestPet requestPetObj;
    public ArrayList<RequestPet> requestPetsArray;
    private FirebaseStorage mStorage;
    private ImageView imgv_petPic;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        setTitle("Pending Request");

        Intent intent = getIntent();
        requestPlaceId = intent.getStringExtra("placeId");
        createRequestPet();

    }

    protected void setTextView(RequestPet requestPetObj){

        dialog = new Dialog(PendingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pending_dialog);

        imgv_petPic = (ImageView)dialog.findViewById(R.id.imgv_petRequest);
        tv_requestPetName = (TextView) dialog.findViewById(R.id.tv_requestPetName);
        tv_requestPetSize = (TextView) dialog.findViewById(R.id.tv_requestPetSize);
        tv_requestPetType = (TextView) dialog.findViewById(R.id.tv_requestPetType);
        tv_requestOwnerEmail = (TextView) dialog.findViewById(R.id.tv_requestOwnerEmail);
        tv_requestStartDate = (TextView) dialog.findViewById(R.id.tv_requestStartDate);
        tv_requestEndDate = (TextView) dialog.findViewById(R.id.tv_requestEndDate);

        Pet petObj = requestPetObj.getPet();
        tv_requestPetName.setText(petObj.getPetName());
        tv_requestPetType.setText(petObj.getPetType());
        tv_requestPetSize.setText(petObj.getPetSize());
        tv_requestStartDate.setText(requestPetObj.getRequestStartDate());
        tv_requestEndDate.setText(requestPetObj.getRequestEndDate());

        Button acceptbt = (Button)dialog.findViewById(R.id.btn_accept);
        acceptbt.setTextColor(Color.parseColor("#ffffff"));
        Button declinebt = (Button)dialog.findViewById(R.id.btn_denied);
        declinebt.setTextColor(Color.parseColor("#ffffff"));

        TextView closebt = (TextView)dialog.findViewById(R.id.skip);
        closebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intentToRecentReq = new Intent(PendingActivity.this, RecentRequestActivity.class);
                startActivity(intentToRecentReq);
                finish();
            }
        });



        //get pet img
        mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        storageRef.child("Owner/"+requestPetObj.getRequestUID_owner() +"/" + petObj.getPetID()+"/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).fitCenter().centerCrop().into(imgv_petPic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Slid", exception.getMessage());
            }
        });

        //get owner email
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Users").child(requestPetObj.getRequestUID_owner()).child("name");
        mQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_requestOwnerEmail.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.show();


    }

    public void sentApprove(View view){
        switch (view.getId()){
            case R.id.btn_accept:
                mDatabase = FirebaseDatabase.getInstance().getReference();
                requestPetObj.setRequestStatus("accept");
                mDatabase.child("RequestPet").child(requestPetObj.getRequestID()).child("requestStatus").setValue(requestPetObj.getRequestStatus());
                mDatabase.child("Sitter").child(requestPetObj.getRequestPlaceID()).child("Client").child(requestPetObj.getRequestID()).setValue(requestPetObj);
                Toast.makeText(this, "Accepted !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_denied:
                requestPetObj.setRequestStatus("denied");
                mDatabase.child("RequestPet").child(requestPetObj.getRequestID()).child("requestStatus").setValue(requestPetObj.getRequestStatus());
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void createRequestPet(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("RequestPet").orderByChild("requestTimeStamp");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                requestPetsArray = new ArrayList<RequestPet>();
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {

                    //check place id same selected && status = pending
                    if (mSnap.child("requestPlaceID").getValue().toString().equals(requestPlaceId)
                            && mSnap.child("requestStatus").getValue().toString().equals("pending")) {
                        requestPetObj = new RequestPet();
                        requestPetObj = mSnap.getValue(RequestPet.class);
                        Log.d("req obj in if>>>>> ", mSnap.toString());
                        requestPetsArray.add(requestPetObj);
                    } //end if
                } // end for

                Log.d("size >>> ...", requestPetsArray.size() + " .............");

                if (requestPetsArray.size() != 0){
                    for (RequestPet reqPet : requestPetsArray){
                        Log.d("a",">>>>>>>>>>>>>array");
                        setTextView(reqPet);
                    }

                }else {
                    Intent intentToRecentReq = new Intent(PendingActivity.this, RecentRequestActivity.class);
                    intentToRecentReq.putExtra("placeId", requestPlaceId);
                    startActivity(intentToRecentReq);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        }); //end value listener

    }


}
