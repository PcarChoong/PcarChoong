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
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
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

                RegisterRequest registerRequest = new RegisterRequest(userID,userPW,userName,userAge,carName,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequest);
            }

        });
    }


}