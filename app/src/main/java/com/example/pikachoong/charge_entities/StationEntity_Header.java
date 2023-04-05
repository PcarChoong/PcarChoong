package com.example.pikachoong.charge_entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "header") //@XmlRootElement : Class에 사용하는 annotation으로 해당 클래스가 XML의 특정 노드의 루트라는 것을 의미
public class StationEntity_Header {
        @XmlElement(name = "resultCode")
        private String resultCode;
        @XmlElement(name = "resultMsg")
        private String resultMsg;
}
