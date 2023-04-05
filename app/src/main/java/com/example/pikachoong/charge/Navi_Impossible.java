package com.example.pikachoong.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pikachoong.AutoCompleteParse;
import com.example.pikachoong.Path;
import com.example.pikachoong.R;
import com.example.pikachoong.RecyclerViewAdapter;
import com.example.pikachoong.SearchEntity;
import com.example.pikachoong.autosearch.Poi;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;



public class Navi_Impossible extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private static final String mApiKey = "EmlFC6GmPM1tAkPEonCFP8qWzAE5UaJQ1FseySqt";
    private TMapView tmapview;
    private TMapGpsManager tmapgps;
    private RecyclerViewAdapter recyclerViewAdapter;
    private double st_lat; // 출발점 위도
    private double st_lon; // 출발점 경도
    private TMapPoint tMapPointStart; // 출발지 지점
    private TMapPoint tMapPointEnd;//목적지 지점
    private TMapData tmapdata;
    private Context mContext = null;
    private float current_remain; // 현재 남아있는 배터리 잔량
    private float need_battery;//도착 후 남아있을 배터리 잔량
    private float fuel_eff;
    protected double distance; // 이동 거리

    private TextView txt;

    private boolean m_bTrackingMode = true;
    private LinearLayout LinearLayoutTmap;
    private ArrayList<SearchEntity> mListData;
    private AutoCompleteParse parse;
    private ArrayList<Poi> p;


    public void onLocationChange(Location location){
        //onLocationChange함수는 일반 메서드보다 호출 순서가 조금 느림 -> onLocationChange를 먼저 수행한 후
        // navigate()함수를 실행하도록 하여야 변수 값의 혼동이 없을 듯.
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
            st_lat = location.getLatitude(); // 현재 위치 위도값 얻어옴
            st_lon = location.getLongitude(); // 현재 위치 경도값 얻어옴

            try {
                navigate();// 지도 위에 polyline 표시
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//          judgement_algor();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_imposs);

        Map();
    }

    public void Map(){
        mContext = this;

        LinearLayoutTmap = findViewById(R.id.linearLayoutTmap);
        tmapview = new TMapView(this);
        LinearLayoutTmap.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);

        tmapview.setIconVisibility(true); // 현위치 아이콘 표시
        tmapview.setZoomLevel(15);//줌레벨
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(this); //단말의 위치 추적을 가능하게 함
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간 설정(단위 : 밀리초)
        tmapgps.setMinDistance(5); // 위치변경 인식 최소거리 설정
        tmapgps.setProvider(tmapgps.GPS_PROVIDER); // gps로 현재 위치를 캐치
        tmapgps.OpenGps(); // 위치 탐색 실시
        tmapview.setTrackingMode(true); // 현 위치를 지도에 표시

    }

    public void navigate() throws ExecutionException, InterruptedException {

        Intent intent = getIntent();
        String mark = intent.getStringExtra("Mark");//"Mark"키 값으로 데이터를 받음
        //입력한 약속장소를 받아옴

        tMapPointStart = new TMapPoint(st_lat, st_lon); // 출발지 좌표 입력(onLocationChange에서 설정한 위도, 경도 값)
        recyclerViewAdapter = new RecyclerViewAdapter();
        parse = new AutoCompleteParse(recyclerViewAdapter); //AutoCompleteParse 객체 생성
        mListData = parse.execute(mark).get(); // 입력한 장소에 대한 전체 SearchEntity 객체 리스트를 반환 및 저장
        this.p = parse.p; // execute함수로 인해 설정된 poi리스트 값을 저장
        // mark와 일치하는 장소의 좌표 객체를 반환

        for(int i=0;i<mListData.size();i++){
            if(mark.equals(p.get(i).getName())){ // 입력한 장소명과 같은 리스트 요소를 찾았다면 그 장소의 위도, 경도값을 목표지점으로 설정
                tMapPointEnd = new TMapPoint(Double.parseDouble(p.get(i).getNoorLat()),Double.parseDouble(p.get(i).getNoorLon()));
            }
        }

        Path_imposs path = new Path_imposs(getApplicationContext(), tmapview);
        distance = path.execute(tMapPointStart, tMapPointEnd).get();//출발지부터 목적지까지 Polyline을 그리고, 그려진 Polyline의 길이를 반환

        TMapMarkerItem endMarkerItem = new TMapMarkerItem();
        TMapPoint tpoint = new TMapPoint(tMapPointEnd.getLatitude(),tMapPointEnd.getLongitude());
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker); // 마커 이미지객체 생성
        endMarkerItem.setTMapPoint(tpoint); // 마커에 point지정
        endMarkerItem.setVisible(TMapMarkerItem.VISIBLE); // 마커가 보일 수 있도록 함
        endMarkerItem.setIcon(bmp); // 아이콘은 미리 지정해둔 이미지로 설정
        tmapview.addMarkerItem("endMarker", endMarkerItem);
        tmapview.setZoomLevel(12);
    }

}