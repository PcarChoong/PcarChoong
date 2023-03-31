package com.example.pikachoong.charge;

import com.example.pikachoong.AutoCompleteParse;
import com.example.pikachoong.Navigate;
import com.skt.Tmap.TMapPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Stations {
    private StringBuilder urlBuilder;
    private String station_addr;
    private Navigate navi;

    Stations(String station_addr){
        this.station_addr = station_addr;
    }

    public ArrayList<TMapPoint> Chargestation() throws IOException {
        navi = new Navigate();

        for(int i=0;i<navi.p.size();i++){
            if(navi.mark.equals(navi.p.get(i).getName())){ 
                station_addr = navi.p.get(i).getUpperAddrName()+navi.p.get(i).getMiddleAddrName();//입력한 장소명의 주소를 받아옴
            }
        }

        urlBuilder = new StringBuilder("http://openapi.kepco.co.kr/service/EvInfoServiceV2/getEvSearchList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "UhKarman7DgUATAB4EIurxt5ch40fMqqTm8MWt3CX+na3+YttYFbg" +
                "/ayLNVgMB6/CXEITNP+36laZcUqY5wYDA=="); // 사전에 제공받은 키 입력
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" +
                URLEncoder.encode("1","UTF-8"));//페이지 번호
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10","UTF-8"));
        //한 페이지 결과 수
        urlBuilder.append("&" + URLEncoder.encode("addr","UTF-8") + "=" + URLEncoder.encode(station_addr, "UTF-8"));
        //검색대상 충전소 주소

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if(conn.getResponseCode()>=200 && conn.getResponseCode()<=300){
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        ArrayList<TMapPoint> sb = new ArrayList<>();
        String line;
        while((line = rd.readLine())!=null){
            sb.add(new TMapPoint(37.5544,127.4632 ));
        }
        rd.close();
        conn.disconnect();
        return sb;
    }
}
