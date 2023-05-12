package com.example.pikachoong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pikachoong.autosearch.Poi;
//import com.example.pikachoong.charge.Navi_Impossible;
import com.example.pikachoong.charge.Navi_Impossible;
import com.example.pikachoong.charge.Navi_Possible;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Navigate extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

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
    private float remain_battery;//도착 후 남아있을 배터리 잔량
    private float fuel_eff; //차량의 연비
    protected double distance; // 이동 거리

    private TextView txt;
    private Button btn_move_navi;

    private boolean m_bTrackingMode = true;
    private LinearLayout LinearLayoutTmap;
    private double batt_remain;
    private ArrayList<SearchEntity> mListData;
    private AutoCompleteParse parse;
    private String battery;
    private String fuel;
    public ArrayList<Poi> p;
    public String mark;


    @Override
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
 //           Move();
            
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        Map(); // 지도 표시

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
        mark = intent.getStringExtra("Mark");//"Mark"키 값으로 데이터를 받음
        //입력한 약속장소를 받아옴

       tMapPointStart = new TMapPoint(st_lat, st_lon); // 출발지 좌표 입력(onLocationChange에서 설정한 위도, 경도 값)

       recyclerViewAdapter = new RecyclerViewAdapter();
       parse = new AutoCompleteParse(recyclerViewAdapter); //AutoCompleteParse 객체 생성
       mListData = parse.execute(mark).get(); // 입력한 장소에 대한 전체 SearchEntity 객체 리스트를 반환 및 저장
       this.p = parse.p; // execute함수로 인해 설정된 poi리스트 값을 저장


      for(int i=0;i<mListData.size();i++){
           if(mark.equals(p.get(i).getName())){ // 입력한 장소명과 같은 리스트 요소를 찾았다면 그 장소의 위도, 경도값을 목표지점으로 설정
                tMapPointEnd = new TMapPoint(Double.parseDouble(p.get(i).getNoorLat()),Double.parseDouble(p.get(i).getNoorLon()));
           }
       }

        Path path = new Path(getApplicationContext(), tmapview); 
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

    public void setNavigate(ArrayList<Poi> p){
        p = this.p;
    }

<<<<<<< HEAD
    public void Move(){
        Intent intent = getIntent();
        fuel = intent.getStringExtra("fuel");
        battery = intent.getStringExtra("battery");
        //infor[0] -> 연비
        //infor[1] -> 배터리 잔량
        batt_remain = Double.parseDouble(battery)*1000.0 - (double)(distance/Double.parseDouble(fuel)); // batt_remain : distance만큼의 거리를 이동하는데 필요한 배터리 용량
        if(batt_remain > 0)
        {
            btn_move_navi = findViewById(R.id.btn_moveable);
            btn_move_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent2 = new Intent(Navigate.this, Navi_Possible.class);
                        intent2.putExtra("Mark", mark);
                        startActivity(intent2); // 해당 화면으로 넘어가기와 값 전달을 동시에 해줌
                }
            });
        } //배터리 잔량이 0보다 크다면 목적지까지 이동 가능하다는 SubActivity로 이동
        else if(batt_remain <= 0)
        {
            btn_move_navi = findViewById(R.id.btn_moveable);
            btn_move_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent1 = new Intent(Navigate.this, Navi_Impossible.class);
                        intent1.putExtra("Mark", mark);
                        startActivity(intent1); // 해당 화면으로 넘어가기와 값 전달을 동시에 해줌
                }
            });
        }



    public void judgement_algor(){
        Intent intent = getIntent();
        ArrayList<String> infor = (ArrayList<String>) intent.getSerializableExtra("information");// 객체를 받아옴
        fuel_eff = Float.parseFloat(infor.get(0));//"F"키 값으로 데이터(연비)를 받음
        current_remain = Float.parseFloat(infor.get(1));//"battery"값으로 데이터(배터리 잔량)을 받음

        remain_battery = current_remain - (float)distance*fuel_eff;

        btn_move_navi.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remain_battery>0){
                    Intent intent = new Intent(Navigate.this , Navi_Possible.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(Navigate.this , Navi_Impossible.class);
                    startActivity(intent);
                }
            }
        }));
//        if((current_remain - need_battery)<=0){
//            Intent intent1 = new Intent(Navigate.this, Navi_Impossible.class);// 현재 배터리 잔량으로 이동 불가능한 경우
//            startActivity(intent1);
//        }else if((current_remain - need_battery)>0){
//
>>>>>>> 6cb3c4fd49ac17872c94ae59aec6b03a7484a82d
    }

//    public void Move(){
//        btn_move_navi = findViewById(R.id.btn_moveable);
//        btn_move_navi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Navigate.this, Navi_Impossible.class);
//                intent.putExtra("Mark", mark);
//                startActivity(intent); // 해당 화면으로 넘어가기와 값 전달을 동시에 해줌
//            }
//        });
//    }

}
