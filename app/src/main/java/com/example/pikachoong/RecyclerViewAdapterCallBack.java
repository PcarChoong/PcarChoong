package com.example.pikachoong;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public interface RecyclerViewAdapterCallBack {
    void getappointment_space(ArrayList<SearchEntity> itemLists, int position);
}
