package com.example.pikachoong.responseimposs;

import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class Geometry {
    private String type;
    private ArrayList<TMapPoint> coordinates;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<TMapPoint> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<TMapPoint> coordinates) {
        this.coordinates = coordinates;
    }
}
