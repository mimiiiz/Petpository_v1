package com.example.petpository_v1.Owner;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SentRequestActivity extends AppCompatActivity {

    EditText startDateET;
    EditText endDateET;
    EditText phone;
    DatePicker datePicker;
    TextView datePlainText;
    Button getDateBtn;
    Button clearDateBtn;
    Dialog dialog;
    Dialog dialogReview;
    String type;
    Pet petData;
    String timestamp;
    DatabaseReference requestRef;
    DateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request);

        format = new SimpleDateFormat("dd-MM-yy");
        timestamp = format.format(new Date());

        startDateET = (EditText) findViewById(R.id.start_date);
        endDateET = (EditText) findViewById(R.id.end_date);
        phone = (EditText) findViewById(R.id.owner_contact);


        dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker_dialog);

        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker_item);
        datePlainText = (TextView) dialog.findViewById(R.id.date_plaintext);
        getDateBtn = (Button) dialog.findViewById(R.id.get_date_button);
        clearDateBtn = (Button) dialog.findViewById(R.id.clear_date_button);
        type = "";
        startDateET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startDateET.setFocusable(true);
                dialog.show();
                type = "from";
                return true;
            }
        });
        endDateET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                endDateET.setFocusable(true);
                dialog.show();
                type = "to";
                return true;
            }
        });

        getDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                int dayOfMouth = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                if (type.equals("from")){
                    startDateET.setText(dayOfMouth+"-"+month+"-"+year);
                }else if (type.equals("to")){
                    endDateET.setText(dayOfMouth+"-"+month+"-"+year);
                }
            }
        });

    }

    public void reviewRequest(View view){
        dialogReview  = new Dialog(this);
        dialogReview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReview.setContentView(R.layout.review_request_dialog);
        dialogReview.getWindow().setBackgroundDrawable(new ColorDrawable(00000000));


        String ownPhone = phone.getText().toString();

        String startDateStr = startDateET.getText().toString();
        String endDateStr = endDateET.getText().toString();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyyy");
        Date startDateReal = new Date(), endDateReal = new Date();
        try {
            startDateReal = df.parse(startDateStr);
            endDateReal = df.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        long diff = endDateReal.getTime() - startDateReal.getTime();
        long diffDay = (diff / (60 * 60 * 1000 * 24 ))+1;
        if (diff >= 0 && ownPhone.length() > 0){
            dialogReview.show();
        }else if (diff < 0){
            Toast.makeText(SentRequestActivity.this, "Invalid date range", Toast.LENGTH_SHORT).show();
        }else if (ownPhone.length() <= 0 || startDateET.getText().toString().length() <= 0 || endDateET.getText().toString().length() <= 0){
            Toast.makeText(SentRequestActivity.this, "Please fill in to continue ", Toast.LENGTH_SHORT).show();
        }

        petData = (Pet) getIntent().getSerializableExtra("pet_data");
        Button confirmButton = (Button) dialogReview.findViewById(R.id.confirm_button);
        final ImageView petImage = (ImageView) dialogReview.findViewById(R.id.my_pet_image);
        TextView tv_msg = (TextView) dialogReview.findViewById(R.id.confirm_msg);
        TextView tv_amountDay = (TextView) dialogReview.findViewById(R.id.confirm_amount_day);


        SharedPreferences spf = getSharedPreferences("current_place", Context.MODE_PRIVATE);
        tv_msg.setText("Send " + petData.getPetName() + " to \n" + spf.getString("place_name",null));
        tv_amountDay.setText(diffDay  + " days ?" );

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("UID", uid);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        StorageReference petImageRef = storageRef.child("Owner").child(uid).child(petData.getPetID()+"/0");
        Log.d("ref", petImageRef.toString());
        petImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(SentRequestActivity.this)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(SentRequestActivity.this))
                        .into(petImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                petImage.setImageResource(R.drawable.ic_pets_black_24dp);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*get pet data*/
                SharedPreferences sp = getSharedPreferences("current_place", Context.MODE_PRIVATE);
                RequestPet newRequestPet = new RequestPet();

                requestRef = FirebaseDatabase.getInstance().getReference().child("RequestPet");
                String genkey = requestRef.push().getKey();

                newRequestPet.setRequestID(genkey);
                newRequestPet.setRequestUID_owner(sp.getString("owner_id",null));
                newRequestPet.setRequestUID_sitter(sp.getString("sitter_id",null));
                newRequestPet.setRequestStatus("pending");
                newRequestPet.setRequestPlaceID(sp.getString("place_id",null));
                newRequestPet.setRequestTimeStamp(timestamp);
                newRequestPet.setRequestStartDate(startDateET.getText().toString());
                newRequestPet.setRequestEndDate(endDateET.getText().toString());
                newRequestPet.setPet(petData);
                newRequestPet.setOwnerPhoneNo(phone.getText().toString());
                newRequestPet.setRequestPlaceName(sp.getString("place_name",null));

                requestRef.child(genkey).setValue(newRequestPet);
                dialogReview.cancel();
                startActivity(new Intent(SentRequestActivity.this, History.class));
                finish();
            }
        });
    }
}
