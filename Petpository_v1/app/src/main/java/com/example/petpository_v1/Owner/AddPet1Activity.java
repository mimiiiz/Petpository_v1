package com.example.petpository_v1.Owner;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;



public class AddPet1Activity extends AppCompatActivity {

    private ImageView imgv_petPhoto;
    private Button btn_addPet;
    private EditText et_petName, et_petBreed;
    private String petName;
    private Pet Pet;
    private ImageButton imgBtn_small, imgBtn_medium, imgBtn_big;
    private String size = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet1);
        setTitle("Add Pet");

        btn_addPet = (Button) findViewById(R.id.btn_addPet);

        et_petName = (EditText) findViewById(R.id.et_petName);
        petName = et_petName.getText().toString();
        et_petName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                petName = et_petName.getText().toString();
                if (!petName.equals("")){
                    btn_addPet.setEnabled(true);
                }else {
                    btn_addPet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pet = new Pet();
                petName = et_petName.getText().toString();
                et_petBreed = (EditText) findViewById(R.id.et_petBreed);
                Pet.setPetName(petName);

                Pet.setPetType(et_petBreed.getText().toString());
                Pet.setPetSize(size);


            }
        });
    }


    public void selectedSize(View view){
        imgBtn_small = (ImageButton) findViewById(R.id.imgBtn_small);
        imgBtn_medium = (ImageButton) findViewById(R.id.imgBtn_medium);
        imgBtn_big = (ImageButton) findViewById(R.id.imgBtn_big);

        switch (view.getId()){
            case R.id.imgBtn_small:
                imgBtn_small.setColorFilter(R.color.colorDivider);
                imgBtn_medium.setColorFilter(Color.argb(1, 185, 183, 183));
                imgBtn_big.setColorFilter(Color.argb(1, 185, 183, 183));
                size = "Small";
                Toast.makeText(AddPet1Activity.this, size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgBtn_medium:
                imgBtn_medium.setColorFilter(R.color.colorDivider);
                imgBtn_small.setColorFilter(Color.argb(1, 185, 183, 183));
                imgBtn_big.setColorFilter(Color.argb(1, 185, 183, 183));
                size = "Medium";
                Toast.makeText(AddPet1Activity.this, size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgBtn_big:
                imgBtn_big.setColorFilter(R.color.colorDivider);
                imgBtn_medium.setColorFilter(Color.argb(1, 185, 183, 183));
                imgBtn_small.setColorFilter(Color.argb(1, 185, 183, 183));
                size = "Large";
                Toast.makeText(AddPet1Activity.this, size, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void pickImgFromAlbum(View view){
//        Intent intent = new Intent(this, AlbumSelectActivity.class);
//        //set limit on number of images that can be selected, default is 10
//        intent.putExtra(SyncStateContract.Constants.INTENT_EXTRA_LIMIT, 5);
//        startActivityForResult(intent, Constants.REQUEST_CODE);

        /*** set img ***/
        imgv_petPhoto = (ImageView) findViewById(R.id.imgv_petPhoto);
//                imgv_petPhoto
//                Pet.setPetPhoto();


    }

}
