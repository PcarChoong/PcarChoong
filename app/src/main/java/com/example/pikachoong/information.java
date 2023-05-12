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

<<<<<<< HEAD
    private Intent intent2;
=======
>>>>>>> 6cb3c4fd49ac17872c94ae59aec6b03a7484a82d
    private Button btn_modify_complete;
    private EditText battery_num;
    private EditText fuel_eff;
    private SearchView searchview;
    private String fuel;
    private String current_remain;
<<<<<<< HEAD
    private String target_space;
=======
>>>>>>> 6cb3c4fd49ac17872c94ae59aec6b03a7484a82d
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        MoveMain();

    }

    public void MoveMain(){
        Intent intent = getIntent();
        btn_modify_complete = findViewById(R.id.btn_modify_complete);
<<<<<<< HEAD
        btn_modify_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                battery_num = findViewById(R.id.battery_num);
                current_remain = battery_num.getText().toString();

                fuel_eff = findViewById(R.id.fuel_eff);
                fuel = fuel_eff.getText().toString();
//                fuel_eff.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        fuel = fuel_eff.getText().toString();
//                    }
//                });
=======
//        battery_num = findViewById(R.id.battery_num);
//        fuel_eff = findViewById(R.id.fuel_eff);
        btn_modify_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    intent = new Intent(information.this, MainActivity.class);
                    //Intent(현재 액티비티(this), 이동할 액티비티(클래스))
                    //이전과 동일한 액티비티로 돌아오기 위해서 같은 이전에 사용했던 intent객체를 사용

                    battery_num = findViewById(R.id.battery_num);
                    battery_num.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            current_remain = battery_num.getText().toString();
                        }
                    });

                    fuel_eff = findViewById(R.id.fuel_eff);
                    fuel_eff.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            fuel = fuel_eff.getText().toString();
                        }
                    });


//                    String fuel = fuel_eff.getText().toString();
//                    String current_remain = battery_num.getText().toString();

                    ArrayList<String> infor = new ArrayList<>();
                    infor.add(fuel);
                    infor.add(current_remain);
//                    intent = new Intent(information.this, Navigate.class);
                    intent.putExtra("information", infor);
>>>>>>> 6cb3c4fd49ac17872c94ae59aec6b03a7484a82d


//                    String fuel = fuel_eff.getText().toString();
//                    String current_remain = battery_num.getText().toString();

               // ArrayList<String> infor = new ArrayList<>();
               // infor.add(fuel);
               // infor.add(current_remain);
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