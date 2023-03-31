package com.example.pikachoong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText edit_id,et_pw;
    private Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id=findViewById(R.id.id_input);
        et_pw=findViewById(R.id.pw_input);
        login=findViewById(R.id.go_log);
        register=findViewById(R.id.go_reg);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserID=edit_id.getText().toString();
                String Userpass=et_pw.getText().toString();

                Response.Listener<String> responseListener= new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                String userName=jsonObject.getString("userName");
                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(Login.this,MainActivity.class);
                                intent.putExtra("username",userName);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                };
                LoginRequest loginRequest = new LoginRequest(UserID,Userpass,responseListener);
                RequestQueue queue= Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);

            }
        });


    }
}