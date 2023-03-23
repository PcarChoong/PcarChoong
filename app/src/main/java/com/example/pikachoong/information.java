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
public class information extends AppCompatActivity {

    private Button btn_modify_complete;
    private EditText battery_num;
    private EditText fuel_eff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Movablility_Judgement();
        MoveMain();

    }

    public void MoveMain(){
        btn_modify_complete = findViewById(R.id.btn_modify_complete);
        btn_modify_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(information.this, MainActivity.class);
                //Intent(현재 액티비티(this), 이동할 액티비티(클래스))
                startActivity(intent1); // activity 이동
            }//btn_modify_complete를 누르면 수행할 동작
        });
    }

    public void Movablility_Judgement(){
        battery_num = findViewById(R.id.battery_num);
        fuel_eff = findViewById(R.id.fuel_eff);
        information infor = new information();
        fuel_eff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String fuel = fuel_eff.getText().toString();
                Intent intent = new Intent(information.this, MainActivity.class);
                intent.putExtra("F", fuel);
            }
        });

        battery_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String current_remain = battery_num.getText().toString();
                Intent intent = new Intent(information.this, MainActivity.class);
                intent.putExtra("battery", current_remain);
            }
        });

    }

//    public void showDialog(){
//        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(information.this).setTitle("경고")
//                .setMessage("모든 정보를 입력하세요.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();//경고창 종료
//                    }
//                });
//    } 연비값과 배터리 잔량값을 모두 입력하지 않고 입력 완료 버튼을 터치할 경우 alert할 경고창 코드
}