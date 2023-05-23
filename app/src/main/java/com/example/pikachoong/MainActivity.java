package com.example.pikachoong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

//import com.example.pikachoong.charge.Navi_Impossible;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private EditText et_appointmentTime;
    private EditText et_appointmentSpace;
    protected EditText et_currentTime;
    private LinearLayout linearLayoutTmap;
    long systemTime = System.currentTimeMillis();
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
    String dTime = formatter.format(systemTime);

    private Context mContext = null;
    private boolean m_bTrackingMode = true;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private static String mApiKey = "EmlFC6GmPM1tAkPEonCFP8qWzAE5UaJQ1FseySqt"; // 발급받은 appKey


    private Button btn_remain;
//    private Button btn_confirm;

    private TMapData tmapdata;
    protected String space;
    private String tg_tm;
    private String t;
    @Override
    public void onLocationChange(Location location){
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //어플을 처음실행할때 가장 먼저 수행하는 부분
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // java파일의 동작을 activity_main에 적용

        currentPoint(); // 현재 위치를 자동으로 지도에 표시

        ShowTimeMethod(); // 현재 시각을 실시간으로 갱신하며 보여줌.(쓰레드 이용)

        MoveRemaining(); // 배터리 잔량입력 버튼 클릭시 배터리 잔량 입력 화면으로 넘어감

        SearchingSpace(); // 약속 장소 입력시, 새로운 페이지로 넘어가 검색을 실행함.

        setappointment_space(); // 약속 장소를 검색하고, 연관 리스트중 선택한 목적지를 텍스트로 나타내줌

//        StartNavigate(); // 목적지까지의 경로 나타내기

        Navigate n = new Navigate();
    }

    public void currentPoint(){
        mContext = this;

        linearLayoutTmap = findViewById(R.id.linearLayoutTmap);// tmap 지도에 값을 입력할 수 있도록 함.
        tmapview = new TMapView(this);
        linearLayoutTmap.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);

        tmapview.setCompassMode(true);//현재 보는 방향
        tmapview.setIconVisibility(true); // 현위치 아이콘 표시
        tmapview.setZoomLevel(15);//줌레벨
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(this); //단말의 위치 추적을 가능하게 함
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간 설정(단위 : 밀리초)
        tmapgps.setMinDistance(5); // 위치변경 인식 최소거리 설정
        tmapgps.setProvider(tmapgps.GPS_PROVIDER); // gps로 현재 위치를 캐치
        tmapgps.OpenGps(); // 위치 탐색 실시

        /*화면 중심을  단말의 현재위치로 이동*/
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);
    }

    public void MoveRemaining(){
        btn_remain = findViewById(R.id.btn_battery);
        et_appointmentTime = (EditText) findViewById(R.id.et_appointmenttime);
        btn_remain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tg_tm = et_appointmentTime.getText().toString();
                Intent intent = new Intent(MainActivity.this, information.class);
                //Intent(현재 액티비티(this), 이동할 액티비티(클래스))
                intent.putExtra("mark", space);//"mark"라는 키값으로 목적지 명 전달
                intent.putExtra("tg_tm", tg_tm);
                startActivity(intent); // activity 이동
            }//btn_batery를 클릭하면 수행할 동작
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void SearchingSpace(){
        et_appointmentSpace = findViewById(R.id.et_appointmentSpace);
        et_appointmentSpace.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(MainActivity.this, SearchView.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    public void ShowTimeMethod(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                et_currentTime = findViewById(R.id.et_currenttime); // et_currentTime 컴포넌트에 값을 입력할 수 있도록 함
                et_currentTime.setText(SimpleDateFormat.getTimeInstance().format(new Date()));
            }
        };
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){e.printStackTrace();}
                    handler.sendEmptyMessage(1);
                }
            }//1초마다 쓰레드 실행(1초마다 갱신하기 위해서)
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void setappointment_space(){
        Intent intent = getIntent(); // 전달한 intent객체를 받음
        space = intent.getStringExtra("space");//"space"키 값으로 데이터를 받음
        et_appointmentSpace = findViewById(R.id.et_appointmentSpace);
        et_appointmentSpace.setText(space);
    }
}