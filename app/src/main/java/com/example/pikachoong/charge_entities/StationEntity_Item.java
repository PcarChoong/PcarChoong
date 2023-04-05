package com.example.pikachoong.charge_entities;

import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "item")
public class StationEntity_Item {
    @XmlElement(name = "addr")
    private String addr; // 충전소 주소
    @XmlElement(name = "chargeTp")
    private int chargeTp;// 1 : 완속 ,2 : 급속
    @XmlElement(name = "cpId")
    private int cpId; // 충전기 ID
    @XmlElement(name = "cpNm")
    private String cpNm;//충전기 명칭
    @XmlElement(name = "cpStat")
    private int cpStat;// 1: 충전가능, 2 : 충전중, 3 : 고장/점검, 4: 통신장애, 5: 통신 미연결
    @XmlElement(name = "cpTp")
    private int cpTp; // 1 : B타입(5핀) 2 : C타입(5핀) 3 : BC타입(5핀) 4 : BC타입(7핀) 5 : DC차데모 6 : AC3상 7 : DC콤보 8 : DC차데모+DC콤보
    @XmlElement(name = "csId")
    private int csId;//충전소 ID
    @XmlElement(name = "csNm")
    private String csNm;//충전소 명칭
    @XmlElement(name = "lat")
    private double lat;//충전소 위도
    @XmlElement(name = "longi")
    private double lon;//충전소 경도
    @XmlElement(name = "statUpdateDatetime")
    private SimpleDateFormat statUpdatetime;//충전기 상태 갱신 시각

}
