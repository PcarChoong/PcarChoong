package com.example.pikachoong.responseimposs;

import java.util.ArrayList;

public class Features {

    private String type;
    private ArrayList<Geometry> geometry;
    private ArrayList<Properties> properties;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Geometry> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<Geometry> geometry) {
        this.geometry = geometry;
    }

    public ArrayList<Properties> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Properties> properties) {
        this.properties = properties;
    }


}
