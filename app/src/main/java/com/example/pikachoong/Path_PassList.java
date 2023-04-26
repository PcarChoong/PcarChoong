package com.example.pikachoong;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class Path_PassList extends AsyncTask<TMapPoint, Void, Double> {
    //TMapPoint : doInBackground 매개변수 타입
    //Void : AsyncTask객체의 onProgressUpdate의 매개변수 타입
    //Double : doInBackground의 리턴 타입
    private Context context;
    private TMapPolyLine tMapPolyLine;
    private TMapView tmapview;
    private ArrayList<TMapPoint> tp;

    public Path_PassList(Context context, TMapView tmapview, ArrayList<TMapPoint> tp){
        super();
        this.context = context;
        this.tmapview = tmapview;
        this.tp = tp;
    }

    @Override
    protected void onPostExecute(Double distance){
        super.onPreExecute();
    }

    @Override
    protected Double doInBackground(TMapPoint... tMapPoints){
         TMapData tmapData = new TMapData();

        try{
            tMapPolyLine = tmapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,tMapPoints[0], tMapPoints[1],tp, 0);
            tMapPolyLine.setLineColor(Color.BLUE);
            tMapPolyLine.setLineWidth(5);
            tmapview.addTMapPolyLine("Line2", tMapPolyLine);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return tMapPolyLine.getDistance(); // 거리 반환 (단위 : m)
    }

}
