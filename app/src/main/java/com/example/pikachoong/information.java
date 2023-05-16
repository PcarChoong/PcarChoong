package com.example.pikachoong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

import java.util.ArrayList;

public class information extends AppCompatActivity {


    private Intent intent2;

    private Button btn_modify_complete;
    private EditText battery_num;
    private EditText fuel_eff;
    private SearchView searchview;
    private String fuel;
    private String current_remain;

    private String target_space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        MoveMain();

    }

    public void MoveMain() {
        btn_modify_complete = findViewById(R.id.btn_modify_complete);

        btn_modify_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                battery_num = findViewById(R.id.battery_num);
                current_remain = battery_num.getText().toString();

                fuel_eff = findViewById(R.id.fuel_eff);
                fuel = fuel_eff.getText().toString();


                intent2 = new Intent(information.this, Navigate.class);
                target_space = intent.getStringExtra("mark");
                intent2.putExtra("fuel", fuel);
                intent2.putExtra("battery", current_remain);
                intent2.putExtra("Mark", target_space);


                startActivity(intent2);
            }//btn_modify_complete를 누르면 수행할 동작

        });
    }
}