package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class Add1PlaceActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Place placeObj;
    private EditText placeName, placeAddress, placeDetail;
    private Double placeLatitude, placeLongtitude;
    private DatabaseReference mDatabase;
    private ArrayList<Image> images;
    private String keyGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add1_place);

//        mDatabase = FirebaseDatabase.getInstance().getReference();

        placeName = (EditText)findViewById(R.id.placename);
        placeAddress = (EditText)findViewById(R.id.address);
        placeDetail = (EditText)findViewById(R.id.detail);

        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");

        images = new ArrayList<>();
        Intent intent = getIntent();
        String address = intent.getStringExtra("placeaddress");
        Toast.makeText(Add1PlaceActivity.this, address, Toast.LENGTH_SHORT).show();
        placeAddress.setText(address);

        placeLatitude = intent.getDoubleExtra("lat", 0);
        placeLongtitude = intent.getDoubleExtra("long", 0);

    }

    public void nexttwoonClick(View v){

        placeObj = new Place();

        keyGen = mDatabase.child("Sitter").push().getKey();
        mDatabase.child("Sitter").child(keyGen).setValue(placeObj);

        storeImage(keyGen, images);

        if (placeName.getText().length()!= 0&&placeAddress.getText().length()!=0&&placeDetail.getText().length()!=0) {
            placeObj.setPlaceName(placeName.getText().toString());
            placeObj.setPlaceAddress(placeAddress.getText().toString());
            placeObj.setPlaceDetail(placeDetail.getText().toString());
            placeObj.setPlaceLatitude(placeLatitude);
            placeObj.setPlaceLongtitude(placeLongtitude);


            Intent intent = new Intent(Add1PlaceActivity.this, Add2PlaceActivity.class);
            intent.putExtra("nameaddObj", placeObj);
            intent.putExtra("keygen", keyGen);
            startActivity(intent);
        }else{
            Toast.makeText(Add1PlaceActivity.this, "Please fill information", Toast.LENGTH_SHORT).show();
        }
    }

    public void createImageBuffer(View v) {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Toast.makeText(Add1PlaceActivity.this, "Add Images Successfully", Toast.LENGTH_SHORT).show();
//            adapter.notifyDataSetChanged();
        }
    }

    private void storeImage(String fileName, ArrayList<Image> images) {
        Log.d("ImageList>>>>>>>>>>", images.toString());

        for (int i = 0; i < images.size(); i++) {
            Log.i("pathhhhhhhhhhh", images.get(i).path);
            Uri file = Uri.fromFile(new File(images.get(i).path));
            Log.d("nn", file.getPath());
            StorageReference imgRef = storageRef.child("Place/" + fileName + "/" + i);
            UploadTask uploadTask = imgRef.putFile(file);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d("FAIL", exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d("NOT FAIL", "FAAAAAAAAAAAAAAAAAAAAAAAFF");
                }
            });
        }
        placeObj.setPlaceNumberImg(images.size());
        Log.d(">>>>>>>>>", images.size()+"");
    }
}