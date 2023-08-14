package com.example.pikachoong.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pikachoong.AutoCompleteParse;
import com.example.pikachoong.R;
import com.example.pikachoong.RecyclerViewAdapter;
import com.example.pikachoong.SearchEntity;
import com.example.pikachoong.autosearch.Poi;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

public class Selected_CS_Path extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private static final String mApiKey = "EmlFC6GmPM1tAkPEonCFP8qWzAE5UaJQ1FseySqt";
    private TMapView tmapview;
    private TMapGpsManager tmapgps;
    private RecyclerViewAdapter recyclerViewAdapter;
    private double st_lat; // 출발점 위도
    private double st_lon; // 출발점 경도
    private double tg_lat; // 목적지 위도
    private double tg_lon; // 목적지 경도
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
    private String mark;
    private int i, n;
    private ArrayList<TMapPoint> tp;
    private String lat, lon;
    private String tg_tm, current_tm;
    private int entire_tm;
    @Override
    public void onLocationChange(Location location){
        //onLocationChange함수는 일반 메서드보다 호출 순서가 조금 느림 -> onLocationChange를 먼저 수행한 후
        // navigate()함수를 실행하도록 하여야 변수 값의 혼동이 없을 듯.
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
            st_lat = location.getLatitude(); // 현재 위치 위도값 얻어옴
            st_lon = location.getLongitude(); // 현재 위치 경도값 얻어옴
            System.out.println("aaaaa");
            try {
                navigate();// 지도 위에 polyline 표시
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            tg_lat = Double.parseDouble(p.get(n).getNoorLat()); // 입력한 목적지의 위도값
            tg_lon = Double.parseDouble(p.get(n).getNoorLon()); // 입력한 목적지의 경도값
            try {
                setTxt();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_cs_path);

        Map();
    }
    public void Map(){
        mContext = this;

        LinearLayoutTmap = findViewById(R.id.linearLayoutTmap4);
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

        Intent intent1 = getIntent();
        lat = intent1.getStringExtra("fs_cs_lat");
        lon = intent1.getStringExtra("fs_cs_lng");
        System.out.println(Double.parseDouble(lat)+"ddss");
        System.out.println(Double.parseDouble(lon)+"dsda");
        //입력한 약속장소를 받아옴

        tMapPointStart = new TMapPoint(st_lat, st_lon); // 출발지 좌표 입력(onLocationChange에서 설정한 위도, 경도 값)
        recyclerViewAdapter = new RecyclerViewAdapter();
        parse = new AutoCompleteParse(recyclerViewAdapter); //AutoCompleteParse 객체 생성
        mListData = parse.execute(mark).get(); // 입력한 장소에 대한 전체 SearchEntity 객체 리스트를 반환 및 저장
        this.p = parse.p; // execute함수로 인해 설정된 poi리스트 값을 저장
        // mark와 일치하는 장소의 좌표 객체를 반환

        for(i=0;i<mListData.size();i++){
            if(mark.equals(p.get(i).getName())){ // 입력한 장소명과 같은 리스트 요소를 찾았다면 그 장소의 위도, 경도값을 목표지점으로 설정
                tMapPointEnd = new TMapPoint(Double.parseDouble(p.get(i).getNoorLat()),Double.parseDouble(p.get(i).getNoorLon()));
                n = i;
            }
        }

        tp = new ArrayList<>();
        tp.add(new TMapPoint(Double.parseDouble(lat), Double.parseDouble(lon))); 

        Path_PassList path_pl = new Path_PassList(getApplicationContext(), tmapview, tp);
        distance = path_pl.execute(tMapPointStart, tMapPointEnd).get();//출발지부터 목적지까지(충전소를 경유하는) Polyline을 그리고, 그려진 Polyline의 길이를 반환

        TMapMarkerItem endMarkerItem = new TMapMarkerItem(); // 목적지를 표시할 마커
        TMapMarkerItem passlistItem = new TMapMarkerItem(); // 경유지를 표시할 마커

        TMapPoint tpoint = new TMapPoint(tMapPointEnd.getLatitude(),tMapPointEnd.getLongitude());
        TMapPoint PLpoint = new TMapPoint(Double.parseDouble(lat), Double.parseDouble(lon)); // 임시로 설정한 값임...

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker); // 마커 이미지객체 생성
        Bitmap bmp2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.charge); // 마커 이미지객체 생성

        endMarkerItem.setTMapPoint(tpoint); // 마커에 point지정
        endMarkerItem.setVisible(TMapMarkerItem.VISIBLE); // 마커가 보일 수 있도록 함
        endMarkerItem.setIcon(bmp); // 아이콘은 미리 지정해둔 이미지로 설정
        tmapview.addMarkerItem("endMarker", endMarkerItem);

        passlistItem.setTMapPoint(PLpoint); // 경유하는 충전소 위치에 마커를 표시
        passlistItem.setVisible(TMapMarkerItem.VISIBLE);
        passlistItem.setIcon(bmp2);
        tmapview.addMarkerItem("PLmarker", passlistItem);
        tmapview.setZoomLevel(12);
    }

    public void setTxt() throws ParseException { // 남은 시간을 구하기 위한 메서드
        Intent intent = getIntent();
        tg_tm = intent.getStringExtra("tg_tm");
        entire_tm = intent.getIntExtra("entire_tm", 0);
        txt = findViewById(R.id.remain_tm); // 이동 가능 여부 알려주는 문구
// 현재 시간
        LocalTime now = null;
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalTime.now();
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            current_tm = now.format(formatter);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date current_dt = sdf.parse(current_tm);
        System.out.println(tg_tm+"dsaaaaaaa");
        Date target_dt = sdf.parse(tg_tm);

        long timeMil1 = current_dt.getTime();
        long timeMil2 = target_dt.getTime();

        long diff = timeMil2 - timeMil1;
        long diff_sec = diff/1000;//도착 희망시각 - 현재 시각 ( = 남은 시간)

        if(diff_sec>entire_tm){ // 남은 시간>총 소요 시간 -> 남은 시간안에 도착이 가능한 겨우
            txt.setText("시간 내에 도착할 수 있어요!");
        }else{ // 남은 시간<= 총 소요 시간 -> 남은 시간 안에 도착이 불가능한 경우
            txt.setText("시간 내에 도착하기 힘들어요.. \n 시간을 조정하는게 좋을 것 같아요.");
        }

    }
}
