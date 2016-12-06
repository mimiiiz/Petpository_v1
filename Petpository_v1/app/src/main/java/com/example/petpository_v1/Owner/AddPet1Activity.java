package com.example.petpository_v1.Owner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.util.ArrayList;


public class AddPet1Activity extends AppCompatActivity {

    private ImageView imgv_petPhoto, imgv_confirm_pet_pic;
    private Button btn_addPet, btn_confirm, btn_back;
    private EditText et_petName, et_petBreed;
    private String petName, keyGenPetID, owner_UID, petType;
    private Pet Pet;
    private ImageButton imgBtn_small, imgBtn_medium, imgBtn_big;
    private String size = "Small";
    private ArrayList<Image> imagesPet1;
    private DatabaseReference mDatabase;
    private DatabaseReference myPetRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Integer check_enableBtn_name = 0, check_enableBtn_type = 0;
    private Uri file;
    private ChildEventListener childEventListener;
    private Dialog confirmAddDialog;
    private TextView tv_confirm_petname, tv_confirm_petBreed, tv_confirm_petSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet1);
        setTitle("Add Pet");

        /** Get User data**/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        owner_UID = mFirebaseUser.getUid();

        /** Get Database Ref **/
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /** Get Storage Ref **/
        storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");

        keyGenPetID = mDatabase.child("Owner").child(owner_UID).child("Pet").push().getKey();
        Pet = new Pet();
//        mDatabase.child("Owner").child(owner_UID).child("Pet").child(keyGenPetID).setValue(Pet);

        et_petName = (EditText) findViewById(R.id.et_petName);
        petName = et_petName.getText().toString();
        et_petName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                petName = et_petName.getText().toString();
                if (!petName.equals("")) {
                    check_enableBtn_name = 1;
                    if (check_enableBtn_name == 1 && check_enableBtn_type == 1) {
                        btn_addPet.setEnabled(true);
                    } else {
                        btn_addPet.setEnabled(false);
                    }
                } else {
                    btn_addPet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_petBreed = (EditText) findViewById(R.id.et_petBreed);
        petType = et_petBreed.getText().toString();
        et_petBreed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                petType = et_petBreed.getText().toString();
                if (!petType.equals("")) {
                    check_enableBtn_type = 1;
                    if (check_enableBtn_name == 1 && check_enableBtn_type == 1) {
                        btn_addPet.setEnabled(true);
                    } else {
                        btn_addPet.setEnabled(false);
                    }
                } else {
                    btn_addPet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgv_petPhoto = (ImageView) findViewById(R.id.imgv_petPhoto);
        imgBtn_small = (ImageButton) findViewById(R.id.imgBtn_small);
        imgBtn_small.setColorFilter(R.color.colorDivider);
        btn_addPet = (Button) findViewById(R.id.btn_addPet);
        imagesPet1 = new ArrayList<>();

        btn_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgv_petPhoto.getVisibility() == View.VISIBLE){
                    //set velue

                petName = et_petName.getText().toString();
                et_petBreed = (EditText) findViewById(R.id.et_petBreed);
                Pet.setPetName(petName);

                Pet.setPetType(et_petBreed.getText().toString());
                Pet.setPetSize(size);
                Pet.setPetID(keyGenPetID);

                setConfirmDialog();
                confirmAddDialog.show();

                btn_confirm = (Button) confirmAddDialog.findViewById(R.id.btn_conf_add);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mDatabase.child("Owner").child(owner_UID).child("Pet").child(keyGenPetID).setValue(Pet);

                        Intent intent = new Intent(AddPet1Activity.this, MyPetsActivity.class);
                        Toast.makeText(AddPet1Activity.this, "Add pet success !", Toast.LENGTH_SHORT).show();

                        childEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                finish();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        myPetRef = mDatabase.child("Owner").child(owner_UID).child("Pet").child(keyGenPetID);
                        myPetRef.addChildEventListener(childEventListener);
                        startActivity(intent);
                        confirmAddDialog.dismiss();
                    }
                });

                btn_back = (Button) confirmAddDialog.findViewById(R.id.btn_conf_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmAddDialog.dismiss();
                    }
                });

                } else {
                    Toast.makeText(AddPet1Activity.this, "Please select image of your pet.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void selectedSize(View view) {
        imgBtn_small = (ImageButton) findViewById(R.id.imgBtn_small);
        imgBtn_medium = (ImageButton) findViewById(R.id.imgBtn_medium);
        imgBtn_big = (ImageButton) findViewById(R.id.imgBtn_big);

        switch (view.getId()) {
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

    public void pickImgFromAlbum(View view) {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);
        startActivityForResult(intent, Constants.REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            imagesPet1 = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Toast.makeText(AddPet1Activity.this, "Add Images Successfully", Toast.LENGTH_SHORT).show();
            storeImage(keyGenPetID, owner_UID, imagesPet1);
        }
    }

    private void storeImage(String fileName, String owner_UID, ArrayList<Image> images) {
        for (int i = 0; i < images.size(); i++) {
            Log.i("pathhhhhhhhhhh", images.get(i).path);
            file = Uri.fromFile(new File(images.get(i).path));
            Log.d("nn", file.getPath());
            StorageReference imgRef = storageRef.child("Owner/" + owner_UID + "/" + fileName + "/" + i);
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

        /*** Set image view ***/

        Glide.with(getApplicationContext()).load(file).fitCenter().centerCrop().into(imgv_petPhoto);
        imgv_petPhoto.setVisibility(View.VISIBLE);
        Log.d("is shown2 ?", (imgv_petPhoto.getVisibility() == View.VISIBLE) + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener != null) {
            myPetRef.removeEventListener(childEventListener);
        }
    }

    public void setConfirmDialog() {
        confirmAddDialog = new Dialog(AddPet1Activity.this);
        confirmAddDialog.setTitle("Confirm add pet");
        confirmAddDialog.setContentView(R.layout.confirm_add_pet_dialog);

        imgv_confirm_pet_pic = (ImageView) confirmAddDialog.findViewById(R.id.imgv_confirm_pet_pic);
        tv_confirm_petname = (TextView) confirmAddDialog.findViewById(R.id.tv_confirm_petname);
        tv_confirm_petBreed = (TextView) confirmAddDialog.findViewById(R.id.tv_confirm_petBreed);
        tv_confirm_petSize = (TextView) confirmAddDialog.findViewById(R.id.tv_confirm_petSize);

        Glide.with(AddPet1Activity.this).load(file).fitCenter().centerCrop().into(imgv_confirm_pet_pic);


        tv_confirm_petname.setText(Pet.getPetName());
        tv_confirm_petBreed.setText(Pet.getPetType());
        tv_confirm_petSize.setText(Pet.getPetSize());
    }
}
