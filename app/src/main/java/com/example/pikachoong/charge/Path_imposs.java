//package com.example.pikachoong.charge;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.AsyncTask;
//
//import com.skt.Tmap.TMapData;
//import com.skt.Tmap.TMapPoint;
//import com.skt.Tmap.TMapPolyLine;
//import com.skt.Tmap.TMapView;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class Path_imposs extends AsyncTask<TMapPoint, Void, Double> {
//    //TMapPoint : doInBackground 매개변수 타입
//    //Void : AsyncTask객체의 onProgressUpdate의 매개변수 타입
//    //Double : doInBackground의 리턴 타입
//    private Context context;
//    private TMapPolyLine tMapPolyLine;
//    private TMapView tmapview;
//    private ArrayList<TMapPoint> ch_stations;
//    private Stations st;
//
//    public Path_imposs(Context context, TMapView tmapview){
//        super();
//        this.context = context;
//        this.tmapview = tmapview;
//    }
//
//    @Override
//    protected void onPostExecute(Double distance){
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Double doInBackground(TMapPoint... tMapPoints){
//        TMapData tmapdata = new TMapData();
//        try {
//            ch_stations = st.Chargestation(); // 충전소를 저장할 배열
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        //  ch_stations.add(new TMapPoint(lat, lon));
//        try{
//            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapPoints[0], tMapPoints[1],
//                    ch_stations, 0, new TMapData.FindPathDataListenerCallback() {
//                        @Override
//                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
//                            tMapPolyLine.setLineColor(Color.BLUE);
//                            tMapPolyLine.setLineWidth(5);
//                            tmapview.addTMapPath(tMapPolyLine);
//                            tmapview.addTMapPolyLine("Line_PASS", tMapPolyLine);
//                        }
//                    }); // 목적지 경로와 경유하는 충전소 알림
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return tMapPolyLine.getDistance(); // 거리 반환 (단위 : m)
//    }
//
//}
