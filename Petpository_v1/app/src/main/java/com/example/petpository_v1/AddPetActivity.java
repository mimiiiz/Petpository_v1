package com.example.petpository_v1;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPetActivity extends AppCompatActivity {

    private ImageButton imgBtn_fromAlbum, imgBtn_fromCamera;
    private ImageView imgv_petPic;
    private Button btn_addPet;
    private Uri uri;
    private String check_notnull = "";

    private static final int REQEUST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        imgv_petPic = (ImageView) findViewById(R.id.imgv_petPhoto);
        imgBtn_fromCamera = (ImageButton) findViewById(R.id.imgBtn_pickFromCamera);
        imgBtn_fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                String imgName = "IMG_" + timeStamp + ".jpg";
                File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/Pet/" + imgName);
                uri = Uri.fromFile(file);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(Intent.createChooser(intent1, "Take a picture with"), REQEUST_CAMERA);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK){
            getContentResolver().notifyChange(uri, null);
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                imgv_petPic.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), uri.getPath(), Toast.LENGTH_SHORT).show();

                if (check_notnull.equals("")){
                    check_notnull = "camera";
                }else if (check_notnull.equals("name")){
                    check_notnull = "confirm";
                }else {
                    Toast.makeText(this, "notnull : " + check_notnull, Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("onActivityResult", e.toString());
            }
        }
    }


    public void pickImgFromAlbum(View v) {

    }




}
