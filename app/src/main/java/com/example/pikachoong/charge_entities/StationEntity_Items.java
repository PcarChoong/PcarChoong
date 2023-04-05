package com.example.pikachoong.charge_entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "items")
public class StationEntity_Items {
    @XmlElement(name = "item")// @XmlElement : 변수에 사용하는 annotation으로 해당 변수가 XML의 노드임을 의미
    public List<StationEntity_Item> ItemList;

    public List<StationEntity_Item> itemList() {
        return ItemList;
    }

}
