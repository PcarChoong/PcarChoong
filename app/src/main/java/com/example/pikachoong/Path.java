package com.example.pikachoong;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

public class Path extends AsyncTask<TMapPoint, Void, Double> {
    //TMapPoint : doInBackground 매개변수 타입
    //Void : AsyncTask객체의 onProgressUpdate의 매개변수 타입
    //Double : doInBackground의 리턴 타입
    private Context context;
    private TMapPolyLine tMapPolyLine;
    private TMapView tmapview;

    public Path(Context context, TMapView tmapview){
        super();
        this.context = context;
        this.tmapview = tmapview;
    }

    @Override
    protected void onPostExecute(Double distance){
        super.onPreExecute();
    }

    @Override
    protected Double doInBackground(TMapPoint... tMapPoints){
        TMapData tmapdata = new TMapData();

        try{
            tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,tMapPoints[0], tMapPoints[1]);
            tMapPolyLine.setLineColor(Color.BLUE);
            tMapPolyLine.setLineWidth(5);
            tmapview.addTMapPolyLine("Line1", tMapPolyLine);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return tMapPolyLine.getDistance(); // 거리 반환 (단위 : m)
    }

}
