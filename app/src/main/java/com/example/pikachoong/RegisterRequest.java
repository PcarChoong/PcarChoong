package com.example.pikachoong;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL ="http://pcarchoong.dothome.co.kr/Register.php";
    private Map<String,String> map;

    public RegisterRequest(String UserID,String UserPW ,String UserName, int UserAge,String CarName, Response.Listener<String> listener)
    {
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();
        map.put("userID",UserID);
        map.put("userPassword",UserPW);
        map.put("userName",UserName);
        map.put("userAge",UserAge+"");
        map.put("car",CarName);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}