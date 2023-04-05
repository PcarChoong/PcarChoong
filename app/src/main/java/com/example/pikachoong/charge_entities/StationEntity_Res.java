package com.example.pikachoong.charge_entities;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.*; // jaxb api를 사용할 수 있도록 함(unmarchalling을 수행할 수 있도록 함)
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD) //XML 데이터를 어떤 방법으로 매핑할지를 선언해줄 수 있는 annotation
@XmlRootElement(name = "response") //@XmlRootElement : Class에 사용하는 annotation으로 해당 클래스가 XML의 특정 노드의 루트라는 것을 뜻합니다.
public class StationEntity_Res {
    @XmlElement(name = "header")
    private StationEntity_Header stHeader;

    public StationEntity_Header HeaderInfo(){
        return stHeader;
    }

    @XmlElement(name = "body")
    private StationEntity_Body stBody;

    public StationEntity_Body bodyInfo() {
        return stBody;
    }

}
