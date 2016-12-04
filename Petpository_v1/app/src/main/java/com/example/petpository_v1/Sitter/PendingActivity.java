package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PendingActivity extends AppCompatActivity {

    private String requestPlaceId;
    private TextView tv_requestPetName, tv_requestOwnerEmail, tv_requestPetSize,
            tv_requestPetType, tv_requestStartDate, tv_requestEndDate;
    private Button btn_accept, btn_denied;
    protected DatabaseReference mDatabase;
    private RequestPet requestPetObj;
    private ArrayList<RequestPet> requestPetArrayList;
    private Pet petObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        setTitle("Pending Request");

        Intent intent = getIntent();
        requestPlaceId = intent.getStringExtra("placeId");

//        requestPetArrayList = new ArrayList<RequestPet>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("RequestPet").orderByChild("requestTimeStamp");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    requestPetObj = new RequestPet();

                    //check place id same selected && status = pending
                    if (mSnap.child("requestPlaceID").getValue().toString().equals(requestPlaceId)
                            && mSnap.child("requestStatus").getValue().toString().equals("pending")) {

                        requestPetObj.setRequestID(mSnap.child("requestID").getValue().toString());
                        requestPetObj.setRequestUID_owner(mSnap.child("requestUID_owner").getValue().toString());
                        requestPetObj.setRequestUID_sitter(mSnap.child("requestUID_sitter").getValue().toString());
                        requestPetObj.setRequestStatus(mSnap.child("requestStatus").getValue().toString());
                        requestPetObj.setRequestPlaceID(mSnap.child("requestPlaceID").getValue().toString());
                        requestPetObj.setRequestPetID(mSnap.child("requestPetID").getValue().toString());
                        requestPetObj.setRequestStartDate(mSnap.child("requestStartDate").getValue().toString());
                        requestPetObj.setRequestEndDate(mSnap.child("requestEndDate").getValue().toString());

                        try {
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                            String reqTimeStampString = mSnap.child("requestTimeStamp").getValue().toString();
                            Date reqTimeStamp = df.parse(reqTimeStampString);
                            requestPetObj.setRequestTimeStamp(reqTimeStamp);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        } //end catch
                        petObj = createPet(requestPetObj.getRequestPetID(), requestPetObj.getRequestUID_owner());

//                        requestPetArrayList.add(requestPetObj);

                    } //end if
                } // end for
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //end value listener


    }

    protected Pet createPet(String requestPetID, String requestUID_owner){
        Log.d("UID Owner>> ", requestPetObj.getRequestUID_owner());
        Log.d("Pet Id >> ", requestPetObj.getRequestPetID());


        petObj = new Pet();
        Query mQ_getPet = mDatabase.child("Owner").child(requestPetObj.getRequestUID_owner()).child("Pet").child(requestPetObj.getRequestPetID()).orderByValue();
        mQ_getPet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Pet>>> ", dataSnapshot.toString());
                petObj.setPetID(dataSnapshot.child("petID").getValue().toString());
                petObj.setPetName(dataSnapshot.child("petName").getValue().toString());
                petObj.setPetType(dataSnapshot.child("petType").getValue().toString());
                petObj.setPetSize(dataSnapshot.child("petSize").getValue().toString());
                setTextView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return petObj;
    }

    protected void setTextView(){

        requestPetObj.getRequestPetID();

        tv_requestPetName = (TextView) findViewById(R.id.tv_requestPetName);
        tv_requestPetSize = (TextView) findViewById(R.id.tv_requestPetSize);
        tv_requestPetType = (TextView) findViewById(R.id.tv_requestPetType);
        tv_requestOwnerEmail = (TextView) findViewById(R.id.tv_requestOwnerEmail);
        tv_requestStartDate = (TextView) findViewById(R.id.tv_requestStartDate);
        tv_requestEndDate = (TextView) findViewById(R.id.tv_requestEndDate);

        tv_requestPetName.setText(petObj.getPetName());
        tv_requestPetType.setText(petObj.getPetType());
        tv_requestPetSize.setText(petObj.getPetSize());
        tv_requestStartDate.setText(requestPetObj.getRequestStartDate().toString());
        tv_requestEndDate.setText(requestPetObj.getRequestEndDate().toString());

    }

    public void sentRequest(View view){
        switch (view.getId()){
            case R.id.btn_accept:
                mDatabase = FirebaseDatabase.getInstance().getReference();
                requestPetObj.setRequestStatus("accept");
                mDatabase.child("RequestPet").child(requestPetObj.getRequestID()).child("requestStatus").setValue(requestPetObj.getRequestStatus());
                mDatabase.child("Sitter").child(requestPetObj.getRequestPlaceID()).child("Client").child(requestPetObj.getRequestID()).setValue(requestPetObj);


                break;
            case R.id.btn_denied:
                break;
        }

    }

}
