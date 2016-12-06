package com.example.petpository_v1.Sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Add2PlaceActivity extends AppCompatActivity {

    private Button backoneBt, nextthreeBt;
    private DatabaseReference mDatabase;
    private EditText et_dogNum, et_startPrice;
    private String workdayAvailiable = "";
    private boolean Small, Medium, Large, monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    private ArrayList<String> sizeDog;
    public Place placeObj;
    private String dognumStr, priceStr, keyGen;
    private Double priceStart;
    private int dogNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2_place);

        Intent intent = getIntent();
        placeObj = new Place();
        placeObj = (Place) intent.getSerializableExtra("nameaddObj");
        keyGen = intent.getStringExtra("keygen");

        backoneBt = (Button)findViewById(R.id.backtoone);
        backoneBt.setText("< BACK");
        nextthreeBt = (Button)findViewById(R.id.nexttothree);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        backoneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextthreeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add2PlaceActivity.this, Add3PlaceActivity.class);

                et_dogNum = (EditText)findViewById(R.id.dognum);
                et_startPrice = (EditText)findViewById(R.id.price);

                monCheck = ((CheckBox) findViewById(R.id.mon)).isChecked();
                tueCheck = ((CheckBox) findViewById(R.id.tue)).isChecked();
                wedCheck = ((CheckBox) findViewById(R.id.wed)).isChecked();
                thuCheck = ((CheckBox) findViewById(R.id.thu)).isChecked();
                friCheck = ((CheckBox) findViewById(R.id.fri)).isChecked();
                satCheck = ((CheckBox) findViewById(R.id.sat)).isChecked();
                sunCheck = ((CheckBox) findViewById(R.id.sun)).isChecked();

                Small = ((CheckBox)findViewById(R.id.small)).isChecked();
                Medium = ((CheckBox)findViewById(R.id.medium)).isChecked();
                Large = ((CheckBox)findViewById(R.id.large)).isChecked();

                dognumStr = et_dogNum.getText().toString();
                priceStr = et_startPrice.getText().toString();

                if (dognumStr != null && priceStr != null){
                    dogNum = Integer.parseInt(dognumStr);
                    priceStart = Double.parseDouble(priceStr);
                    if ((dogNum <= 0 && dogNum >1000)||(priceStart <= 0 && priceStart > 50000)){
                        if (dogNum <= 0 && dogNum >1000) {
                            Toast.makeText(Add2PlaceActivity.this, "Max number of dog must be between 1-1,000", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Add2PlaceActivity.this, "Price must be between 1-50,000 baht", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        placeObj.setPlaceDogNum(dogNum);
                        placeObj.setPlacePrice(priceStart);
                        placeObj.setPlaceWorkDay(checkWorkday());
                        placeObj.setPlaceDogSize(checkSize());

                        intent.putExtra("numpricedaysize", placeObj);
                        intent.putExtra("KeyGen", keyGen);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(Add2PlaceActivity.this, "Please fill information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public String checkWorkday() {
        if (monCheck && tueCheck && wedCheck && thuCheck && friCheck && satCheck && sunCheck) {
            //mon-sun
            workdayAvailiable = "Everyday";
        } else if (monCheck && thuCheck && wedCheck && thuCheck && friCheck) {
            //mon-fri
            workdayAvailiable = "Monday to Friday";
        } else {
            if (monCheck) {workdayAvailiable += "Monday ";}
            if (tueCheck) {workdayAvailiable += "Tuesday ";}
            if (wedCheck) {workdayAvailiable += "Wednesday ";}
            if (thuCheck) {workdayAvailiable += "Thursday ";}
            if (friCheck) {workdayAvailiable += "Friday ";}
            if (satCheck) {workdayAvailiable += "Saturday ";}
            if (sunCheck) {workdayAvailiable += "Sunday";}
        }
        return workdayAvailiable;
    }
    public ArrayList<String> checkSize(){
        //checksize
        sizeDog = new ArrayList<String>(3);
        if (Small){sizeDog.add("Small");}
        if (Medium){sizeDog.add("Medium");}
        if (Large){sizeDog.add("Large");}
        return sizeDog;
    }
}