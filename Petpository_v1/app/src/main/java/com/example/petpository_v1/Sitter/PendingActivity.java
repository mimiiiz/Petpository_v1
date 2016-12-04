package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        setTitle("Pending Request");

        Intent intent = getIntent();
        requestPlaceId = intent.getStringExtra("placeId");

        requestPetArrayList = new ArrayList<RequestPet>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("RequestPet").orderByChild("requestTimeStamp");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    requestPetObj = new RequestPet();
                    Log.d("in for >>>>>>>>>>", "............");

                    //check place id same selected && status = pending
                    if (mSnap.child("requestPlaceID").getValue().toString().equals(requestPlaceId)
                            && mSnap.child("requestStatus").getValue().toString().equals("pending")) {
                        Log.d("in if >>>>>>>>>>", "............");

                        requestPetObj.setRequestID(mSnap.child("requestID").getValue().toString());
                        requestPetObj.setRequestUID_owner(mSnap.child("requestUID_owner").getValue().toString());
                        requestPetObj.setRequestUID_sitter(mSnap.child("requestUID_sitter").getValue().toString());
                        requestPetObj.setRequestUID_owner(mSnap.child("requestStatus").getValue().toString());
                        requestPetObj.setRequestPlaceID(mSnap.child("requestPlaceID").getValue().toString());
                        requestPetObj.setRequestPetID(mSnap.child("requestPetID").getValue().toString());

                        try {
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                            String reqTimeStampString = mSnap.child("requestTimeStamp").getValue().toString();
                            Date reqTimeStamp = df.parse(reqTimeStampString);

                            String reqStartString = mSnap.child("requestStartDate").getValue().toString();
                            Date reqStartDate = df.parse(reqStartString);

                            String reqEndString = mSnap.child("requestEndDate").getValue().toString();
                            Date reqEndDate = df.parse(reqEndString);

                            requestPetObj.setRequestTimeStamp(reqTimeStamp);
                            requestPetObj.setRequestStartDate(reqStartDate);
                            requestPetObj.setRequestEndDate(reqEndDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        } //end catch
                        requestPetArrayList.add(requestPetObj);
                        Log.d("ADDDDDDDDDDD >", "............");

                    } //end if
                } // end for
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //end value listener

        Log.d("arrr >>>>>>>>>>", requestPetArrayList.size() + "");

        for (RequestPet requestPet : requestPetArrayList){
            Log.d("Pet>>>>>", requestPet.getRequestID());
        }


    }
}
