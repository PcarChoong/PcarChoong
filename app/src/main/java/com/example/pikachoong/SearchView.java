package com.example.pikachoong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pikachoong.autosearch.Poi;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.poi_item.TMapPOIItem;
import java.util.*;

public class SearchView extends AppCompatActivity implements RecyclerViewAdapterCallBack {

    private EditText edt_search;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper()); // Looper, handler객체를 이용하여 멀티쓰레드 환경 구축
    private Runnable workRunnable;
    protected ArrayList<Poi> p;
    private final long DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        layoutInit();
    }

    @Override
    public int getappointment_space(ArrayList<SearchEntity> itemLists,int position){
        String appointment_space = itemLists.get(position).getTitle();
        Intent intent1 = new Intent(SearchView.this, MainActivity.class);
        intent1.putExtra("space",appointment_space); // "space" 키 값을 이용해 입력한 appointment_space 값을 intent로 이동시킨다.
        startActivity(intent1);
        return position;
    }

    private void layoutInit(){
        edt_search = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.rl_listview);

        DividerItemDecoration div_Itemdecor = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(div_Itemdecor); // 구분선 삽입(데코레이션 이용)

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable) {
                final String keyword = editable.toString();

                handler.removeCallbacks(workRunnable);
                workRunnable = new Runnable(){
                    @Override
                    public void run(){
                        adapter.filter(keyword); // 2글자 이상 검색창에 입력시, 검색 리스트에 관련 리스트 요소가 추가
                    }
                };
                handler.postDelayed(workRunnable, DELAY);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this);
    }
}