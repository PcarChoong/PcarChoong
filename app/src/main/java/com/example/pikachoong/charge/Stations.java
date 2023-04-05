package com.example.pikachoong.charge;

import com.example.pikachoong.Navigate;
import com.example.pikachoong.charge_entities.StationEntity_Res;
import com.skt.Tmap.TMapPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class Stations {
    private final String DataKey= "UhKarman7DgUATAB4EIurxt5ch40fMqqTm8MWt3CX%2Bna3%2BYttYFbg%2FayLNVgMB6%2FCXEITNP%2B36laZcUqY5wYDA%3D%3D";
    private StringBuilder urlBuilder;
    private String station_addr;
    private Navigate navi;
    private StationEntity_Res stations;
    private ArrayList<TMapPoint> ch_tMapPoints;

    public ArrayList<TMapPoint> Chargestation() throws IOException {
        navi = new Navigate();

        for(int i=0;i<navi.p.size();i++){
            if(navi.mark.equals(navi.p.get(i).getName())){ 
                station_addr = navi.p.get(i).getUpperAddrName()+navi.p.get(i).getMiddleAddrName();//입력한 장소명의 주소를 받아옴
            }
        }

        urlBuilder = new StringBuilder("http://openapi.kepco.co.kr/service/EvInfoServiceV2/getEvSearchList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + DataKey); // 사전에 제공받은 키 입력
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
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        }else{
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = rd.readLine())!=null){
            sb.append(line);
        }

        //해야할 것 : 응답 받은 값 : sb값(xml형태)을 arraylist형태로 저장하여 각 충전소별 위도, 경도값을 ArrayList<TMqpPoint>형식으로 저장하기
        rd.close();
        conn.disconnect();

        String xml = sb.toString();

        //Map<String, StationEntity_Res> result = new HashMap<>();
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(StationEntity_Res.class); //jaxb context 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller(); //Unmarshaller객체 생성(xml -> java 객체 변환 과정 수행)
            stations = (StationEntity_Res) unmarshaller.unmarshal(new StringReader(xml)); // unmarshaller 작업 수행 및 응답정보를 객체에 저장


            //result.put("response", stations);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ;
    }
}
