package com.example.petpository_v1.Owner;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.petpository_v1.R;

public class SentRequestActivity extends AppCompatActivity {

    EditText startDateET;
    EditText endDateET;
    EditText phone;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request);

        startDateET = (EditText) findViewById(R.id.start_date);
        endDateET = (EditText) findViewById(R.id.end_date);
        phone = (EditText) findViewById(R.id.owner_contact);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker_dialog);
        startDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    dialog.show();
                }
            }
        });
        endDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    dialog.show();
                }
            }
        });

    }
}
