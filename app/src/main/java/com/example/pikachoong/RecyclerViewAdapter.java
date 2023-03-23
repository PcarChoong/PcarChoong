package com.example.pikachoong;


import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pikachoong.autosearch.Poi;

import java.util.ArrayList;
import java.util.Collection;

//RecyclerView는 반복되는 아이템 리스트를 데이터만 바꾸어 보여줌
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<SearchEntity> itemLists = new ArrayList<>();

    private RecyclerViewAdapterCallBack callback;
    private AutoCompleteParse parser;


    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView address;

        public CustomViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            address = itemView.findViewById(R.id.tv_address);
        }
    }
   //list_item.xml을 띄워줌
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        //LayoutInflater은 xml파일의 리소스를 view객체로 반환해주며, from()메서드를 통해 LayoutInflater객체 얻음
        //inflate(R.layout.sub1, container, boolean bool)메서드에서 sub1은 xml레이아웃 리소스이고,
        // container는 부모 컨테이너이다.
        // container레이아웃에 부분화면으로 띄워주기 위해 만든 sub1.xml을 띄워준다.
        // bool이 true이면 현재 view를 parent에 즉시 추가, false이면 parent에 '나중에' 추가
        return new CustomViewHolder(view);
    }

    //가져온 데이터를 아이템뷰에 적용(Binding)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        final int ItemPosition = position;

        if(holder instanceof CustomViewHolder){
            CustomViewHolder viewHolder = (CustomViewHolder)holder;

            viewHolder.title.setText(itemLists.get(ItemPosition).getTitle());
            viewHolder.address.setText(itemLists.get(ItemPosition).getAddress());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                  callback.getappointment_space(itemLists, ItemPosition);
                }//리스트 요소 중 하나를 클릭시, itemLists의 요소중 ItemPosition위치에 있는 요소값을 MainActivity로 받아옴
            });//아이템 클릭 시 수행
        }
    }
    @Override
    public int getItemCount(){
        return itemLists.size();
    }

    public void setData(ArrayList<SearchEntity> itemLists){
        this.itemLists = itemLists;
    } // 본 객체에 매개변수로 받아온 SearchEntity 객체를 저장할 수 있도록 함.

    public void setCallback(RecyclerViewAdapterCallBack callback) {
        this.callback = callback;
    }

    public void filter(String keyword){
        if(keyword.length()>=2) {
            try {
                parser = new AutoCompleteParse(this);
                itemLists.addAll(parser.execute(keyword).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
