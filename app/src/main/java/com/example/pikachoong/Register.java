package com.example.pikachoong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private EditText edit_id,et_pw,et_name,et_age;
    private Button btn_register;
    String C;
    float Cona_batt; // 현대 코나 일렉트릭(1세대) 배터리 용량
    float ionic_batt; // 현대 아이오닉 6(1세대) 배터리 용량
    float niro_batt; // 니로 EV(2세대) 배터리 용량
    float shav_batt; // 쉐보레 볼트 EV(1세대) 배터리 용량
    float sm3_batt; // 르노삼성 SM3 배터리 용량
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edit_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);

        spinner = findViewById(R.id.car_spinner);
        ArrayAdapter carAdapter = ArrayAdapter.createFromResource(this, R.array.car_array, android.R.layout.simple_spinner_dropdown_item);
        carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(carAdapter);



        btn_register=findViewById(R.id.button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID=edit_id.getText().toString();
                String userPW=et_pw.getText().toString();
                String userName =et_name.getText().toString();
                int userAge=Integer.parseInt(et_age.getText().toString());
                String carName=spinner.getSelectedItem().toString();
                C = carName;
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            if(success)
                            {
                                Toast.makeText(getApplicationContext(),"회원가입 성공.",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"회원가입 실패.",Toast.LENGTH_SHORT).show();
                                return ;
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                if(carName.equals("현대 코나 일렉트릭(1세대)")){
                    Cona_batt = 77.4f; // 코나 : 77.4kWh용량을 가짐
                }else if(carName.equals("현대 아이오닉6(1세대)")){
                    ionic_batt = 77.4f; // 아이오닉6 : 77.4kWh용량을 가짐
                }else if(carName.equals("니로 EV(2세대)")){
                    niro_batt = 64;
                }else if(carName.equals("쉐보레 볼트 EV (1세대)")){
                    shav_batt = 65;
                }else if(carName.equals("르노삼성 sm3 z.e(2세대)")){
                    sm3_batt = 35.9f;
                }
                RegisterRequest registerRequest = new RegisterRequest(userID,userPW,userName,userAge,carName,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequest);
            }

        });
    }
    public float getCona_batt(){return Cona_batt;}
    public float getIonic_batt(){return ionic_batt;}
    public float getNiro_batt(){return niro_batt;}
    public float getShav_batt(){return shav_batt;}
    public float getSm3_batt(){return sm3_batt;}
    public String getC(){return C;}

}