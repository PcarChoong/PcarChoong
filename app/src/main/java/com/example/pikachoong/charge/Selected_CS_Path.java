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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

public class Selected_CS_Path extends AppCompatActivity {

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
    private ArrayList<TMapPoint> tp = new ArrayList<>();
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


            tg_lat = Double.parseDouble(p.get(n).getNoorLat()); // 입력한 목적지의 위도값
            tg_lon = Double.parseDouble(p.get(n).getNoorLon()); // 입력한 목적지의 경도값

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_cs_path);

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
        // mark와 일치하는 장소의 좌표 객체를 반환

        for(i=0;i<mListData.size();i++){
            if(mark.equals(p.get(i).getName())){ // 입력한 장소명과 같은 리스트 요소를 찾았다면 그 장소의 위도, 경도값을 목표지점으로 설정
                tMapPointEnd = new TMapPoint(Double.parseDouble(p.get(i).getNoorLat()),Double.parseDouble(p.get(i).getNoorLon()));
                n = i;
            }
        }


        tp.add(new TMapPoint(37.551882, 127.087532)); // 임시로 설정한 값임...

        Path_PassList path_pl = new Path_PassList(getApplicationContext(), tmapview, tp);
        distance = path_pl.execute(tMapPointStart, tMapPointEnd).get();//출발지부터 목적지까지(충전소를 경유하는) Polyline을 그리고, 그려진 Polyline의 길이를 반환

        TMapMarkerItem endMarkerItem = new TMapMarkerItem(); // 목적지를 표시할 마커
        TMapMarkerItem passlistItem = new TMapMarkerItem(); // 경유지를 표시할 마커

        TMapPoint tpoint = new TMapPoint(tMapPointEnd.getLatitude(),tMapPointEnd.getLongitude());
        TMapPoint PLpoint = new TMapPoint(37.551882, 127.087532); // 임시로 설정한 값임...

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
}