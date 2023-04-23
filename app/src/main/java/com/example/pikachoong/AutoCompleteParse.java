package com.example.pikachoong;

import android.os.AsyncTask;

import com.example.pikachoong.autosearch.Poi;
import com.example.pikachoong.autosearch.TMapSearchInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class AutoCompleteParse extends AsyncTask<String, Void, ArrayList<SearchEntity>>{
    private final String TMAP_API_KEY = "EmlFC6GmPM1tAkPEonCFP8qWzAE5UaJQ1FseySqt";
    private final int SEARCH_COUNT = 20; // 페이지당 검색 결과 수 = 20으로 지정
    protected ArrayList<SearchEntity> mListData;
    protected RecyclerViewAdapter mAdapter;

    public ArrayList<Poi> p;
    private String fullAddr;
    public AutoCompleteParse(RecyclerViewAdapter adapter){
        this.mAdapter = adapter;
        mListData = new ArrayList<SearchEntity>();
    }

    @Override
    protected ArrayList<SearchEntity> doInBackground(String... word){
        return getAutoComplete(word[0]);
    }

    protected void onPostExecute(ArrayList<SearchEntity> autoComplete){
        mAdapter.setData(autoComplete);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<SearchEntity> getAutoComplete(String word){
        try{
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            URL acURL = new URL(
                    "https://apis.openapi.sk.com/tmap/pois?version=1&"+
                            "searchKeyword=" + encodeWord + "&searchType=all&searchtypCd=A&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&page=1&"+
                            "count="+SEARCH_COUNT+"&multiPoint=N&poiGroupYn=N"
                    //장소(POI(Point of Interest))통합 검색을 위한 url 작성
            );
            HttpURLConnection acConn = (HttpURLConnection) acURL.openConnection();
            acConn.setRequestProperty("Accept", "application/json"); // Header 부분. 응답형식은 json방식으로 함
            acConn.setRequestProperty("appKey",TMAP_API_KEY); // api키를 header에 지정.
            BufferedReader reader = new BufferedReader(new InputStreamReader(acConn.getInputStream()));

            String line = reader.readLine();
            if(line == null){
                mListData.clear();
                return mListData;
            }

            reader.close();
            mListData.clear();

            TMapSearchInfo searchPoiInfo = new Gson().fromJson(line, TMapSearchInfo.class);
            //읽어들인 라인을 json표현으로 변환


            p = searchPoiInfo.getSearchPoiInfo().getPois().getPoi();
            for(int i = 0;i<p.size();i++){
                fullAddr = p.get(i).getUpperAddrName()+ " "+p.get(i).getMiddleAddrName() +
                        " "+p.get(i).getLowerAddrName();

                mListData.add(new SearchEntity(p.get(i).getName(),fullAddr));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mListData;
    }

}
