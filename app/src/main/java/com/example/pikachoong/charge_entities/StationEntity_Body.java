package com.example.pikachoong.charge_entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "body")
public class StationEntity_Body {
    @XmlElement(name = "items")
    private StationEntity_Items items;
    @XmlElement(name = "numOfRows")
    private String numOfRows;
    @XmlElement(name = "pageNo")
    private String pageNo;
    @XmlElement(name = "totalCount")
    private String totalCount;

    public StationEntity_Items getItems(){
        return items;
    }
}
