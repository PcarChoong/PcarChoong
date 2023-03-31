package com.example.pikachoong;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL ="http://pcarchoong.dothome.co.kr/Login.php";
    private Map<String,String> map;

    public LoginRequest(String UserID, String UserPW , Response.Listener<String> listener)
    {
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();
        map.put("userID",UserID);
        map.put("userPassword",UserPW);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}