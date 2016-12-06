package com.example.petpository_v1.Owner;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.example.petpository_v1.adapter.SlideImageAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback{
    private ImageView smallImg, mediumImg, largeImg;
    private TextView name, detail, workday, price;
    private Button bookbt;
    private ImageButton phonebt, websitebt, linebt, facebookbt, instagrambt;
    private String placeName,placeAddress,placeTel,placeDetail,placeWebsite,placeFacebook, placeLine,placeIg,placePrice,placeWorkDay;
    private Double placeLatitude, placeLongtitude;
    private ArrayList<String> dogSize;
    private Place place;

    final Context context = this;
    private Button button;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        dogSize = new ArrayList<>();
        setValue();


        //textview
        name = (TextView) findViewById(R.id.nametv);
        detail = (TextView) findViewById(R.id.detailtv);
        workday = (TextView) findViewById(R.id.workdaytv);
        price = (TextView) findViewById(R.id.pricetv);

        //image button
        phonebt = (ImageButton) findViewById(R.id.phone);
        websitebt = (ImageButton) findViewById(R.id.website);
        facebookbt = (ImageButton) findViewById(R.id.facebook);
        linebt = (ImageButton) findViewById(R.id.line);
        instagrambt = (ImageButton) findViewById(R.id.instagram);
        bookbt = (Button) findViewById(R.id.bookbt);

        //setText to TextView
        name.setText(placeName);
        detail.setText(placeDetail);
        price.setText(placePrice);
        workday.setText(placeWorkDay);

        largeImg  = (ImageView)findViewById(R.id.large);
        mediumImg = (ImageView)findViewById(R.id.medium);
        smallImg = (ImageView)findViewById(R.id.small);
        setDogSizeImage();

        // **************** ImageSlider ********************
        ViewPager mViewPager = (ViewPager) findViewById(R.id.placeimg);
        SlideImageAdapter adapterView = new SlideImageAdapter(this, place.getPlaceNumberImg(), place.getPlaceId());
        mViewPager.setAdapter(adapterView);


        // ******************** Map ************************
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public void setValue(){
        Intent intent = getIntent();
        place = (Place)intent.getSerializableExtra("place");
        placeName = place.getPlaceName();
        placeTel = place.getPlaceTel();
        placeWebsite = place.getPlaceWebsite();
        placeFacebook = place.getPlaceFacebook();
        placeLine = place.getPlaceLine();
        placeIg = place.getPlaceIg();
        placeAddress = place.getPlaceAddress();
        placeDetail = place.getPlaceDetail();
        placePrice = place.getPlacePrice().toString();
        placeWorkDay = place.getPlaceWorkDay();
        placeLatitude = place.getPlaceLatitude();
        placeLongtitude = place.getPlaceLongtitude();
        dogSize = place.getPlaceDogSize();
    }

    //set each dogsize to ImageView
    public void setDogSizeImage(){
        for(int i=0; i<dogSize.size(); i++){
            if(dogSize.get(i).equals("Small")){
                smallImg.setImageResource(R.drawable.small);
            }if(dogSize.get(i).equals("Medium")){
                mediumImg.setImageResource(R.drawable.medium);
            }if(dogSize.get(i).equals("Large")){
                largeImg.setImageResource(R.drawable.large);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(placeLatitude, placeLongtitude);
        mMap.addMarker(new MarkerOptions().position(location).title(placeName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
    }

    //open dial keypad
    public void onClickPhone(View v){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+placeTel));
        if (ActivityCompat.checkSelfPermission(PlaceDetailActivity.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PlaceDetailActivity.this,
                    android.Manifest.permission.CALL_PHONE)) {
            }
        }
        startActivity(callIntent);
    }

    //open facebook app or facebook link on browser
    public void onClickFacebook(View v){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=http://"+ placeFacebook));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+placeFacebook)));
        }
    }

    //open web browser
    public void onClickWebsite(View v){
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+placeWebsite));
        startActivity(launchBrowser);
    }

    //open dialog line
    public void onClickLine(View v){
        TextView showText = new TextView(this);
        showText.setText("LineId: "+placeLine);
        showText.setTextSize(16);
        showText.setPadding(80,50,0,0);
        showText.setTextIsSelectable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Build the Dialog
        builder.setView(showText).setTitle("Line").setCancelable(true);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //open instagram app or instagram link on browser
    public void onClickInstagram(View v){
        Uri uri = Uri.parse("http://instagram.com/_u/"+placeIg);
        Intent launchInstagram = new Intent(Intent.ACTION_VIEW, uri);
        launchInstagram.setPackage("com.instagram.android");

        try {
            startActivity(launchInstagram);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+placeIg)));
        }
    }

    public void onClickBook(View v){
        Intent intent = new Intent(this, ChoosePetActivity.class);
        intent.putExtra("place", place);
        startActivity(intent);
    }


}
