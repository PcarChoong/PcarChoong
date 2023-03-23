package com.example.pikachoong;

import com.example.pikachoong.autosearch.Poi;

public class SearchEntity extends Poi {
    private String title;
    private String address;

    public SearchEntity(String title, String address){
        this.title = title;
        this.address = address;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
}
