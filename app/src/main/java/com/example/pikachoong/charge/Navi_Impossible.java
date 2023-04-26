package com.example.pikachoong.charge;

import static org.json.JSONObject.NULL;

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
import com.example.pikachoong.Path;
import com.example.pikachoong.Path_PassList;
import com.example.pikachoong.R;
import com.example.pikachoong.RecyclerViewAdapter;
import com.example.pikachoong.SearchEntity;
import com.example.pikachoong.autosearch.Poi;
import com.example.pikachoong.responseimposs.Features;
import com.example.pikachoong.responseimposs.Properties;
import com.example.pikachoong.responseimposs.TMapCarPath;
import com.google.gson.Gson;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.address_info.TMapAddressInfo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;


public class Navi_Impossible extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

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
    private Path path;
    private String address;
    private String line;

    private int time;
    private ArrayList<TMapPoint> tp;
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

            tg_lat = Double.parseDouble(p.get(n).getNoorLat()); // 입력한 목적지의 위도값
            tg_lon = Double.parseDouble(p.get(n).getNoorLon()); // 입력한 목적지의 경도값
            try{
                PathTime();

            } catch (IOException | ParserConfigurationException | SAXException | JSONException e) {
                throw new RuntimeException(e);
            }
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

        LinearLayoutTmap = findViewById(R.id.linearLayoutTmap2);
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
        // mark와 일치하는 장소의 좌표 객체를 반환

        for(i=0;i<mListData.size();i++){
            if(mark.equals(p.get(i).getName())){ // 입력한 장소명과 같은 리스트 요소를 찾았다면 그 장소의 위도, 경도값을 목표지점으로 설정
                tMapPointEnd = new TMapPoint(Double.parseDouble(p.get(i).getNoorLat()),Double.parseDouble(p.get(i).getNoorLon()));
                n = i;
            }
        }

        tp = new ArrayList<>();
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

    public int PathTime() throws IOException, ParserConfigurationException, SAXException, JSONException { // 경유지를 경유하는 경로에 대한 예상 소요시간을 반환(위의 navigate()함수를 수행한 다음에 수행되어야 함)
        tmapdata = new TMapData();
        TMapAddressInfo addressInfor = tmapdata.reverseGeocoding(st_lat,st_lon, "A00" ); // 현재 위치의 좌표를 바탕으로 주소를 알아냄(리버스 지오코딩)
        String encodeStWord = URLEncoder.encode("잠실역", "UTF-8");

        LocalDate now1 = null;
        LocalTime now2 = null;
        String formattedNowD= null;
        String formattedNowT=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now1 = LocalDate.now();
            now2 = LocalTime.now();
            // 포맷 정의
            DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("YYYYMMdd");
            DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HHmmss");
            // 포맷 적용
            formattedNowD = now1.format(formatterD);
            formattedNowT = now2.format(formatterT);

        }

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"tollgateFareOption\":16,\"roadType\":32,\"directionOption\":1,\"endX\":"+tg_lon+",\"endY\":"+tg_lat+"," +
                "\"endRpFlag\":\""+p.get(n).getRpFlag()+"\",\"reqCoordType\":\"WGS84GEO\",\"startX\":"+st_lon+"," +
                "\"startY\":"+st_lat+",\"gpsTime\":\""+formattedNowD+formattedNowT+"\",\"speed\":10,\"uncetaintyP\":1," +
                "\"uncetaintyA\":1,\"uncetaintyAP\":1,\"carType\":0," +
                "\"startName\":\""+ encodeStWord+"\"," +
                "\"endName\":\""+URLEncoder.encode(p.get(n).getName(), "UTF-8")+"\"," +
                "\"passList\":\"127.087532,37.551882\"," +
                "\"gpsInfoList\":\"126.939376564495,37.470947057194365,"+formattedNowT+",20,50,5,2,12,1_126.939376564495,37.470947057194365,"+formattedNowT+",20,50,5,2,12,1\"," +
                "\"detailPosFlag\":\"2\",\"resCoordType\":\"WGS84GEO\",\"sort\":\"index\"}");


        Request request = new Request.Builder()
                .url("https://apis.openapi.sk.com/tmap/routes?version=1")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("appKey", mApiKey)
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}
            @Override
            public void onResponse(Response response) throws IOException {
              line = response.body().string();
            }
        }); // 응답값들을 비동기 처리함(실시간으로 예상 소요시간을 받아와야 하기 때문)
        System.out.println("Hello   :   "+line);
        if(line !=null) {
            JSONObject json = new JSONObject(line);
            JSONArray feat = json.getJSONArray("features");
            JSONObject prop = feat.getJSONObject(0).getJSONObject("properties");
            time = prop.getInt("totalTime");
        }
        methodT(time+"");

        return 1;
    }

    public void methodT(String line){
        txt = findViewById(R.id.PATH_time);
        txt.setText(line);
    }
}