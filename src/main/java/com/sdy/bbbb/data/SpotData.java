package com.sdy.bbbb.data;


import com.sdy.bbbb.entity.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotData extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //핫스팟 장소명
    @Column(nullable = false)
    private String areaNm;

    //장소 혼잡도 지표
    @Column(nullable = false)
    private String areaCongestLvl;

    //실시간 인구 지표 최소값
    @Column(nullable = false)
    private String areaPpltnMin;

    //실시간 인구 지표 최대값
    @Column(nullable = false)
    private String areaPpltnMax;

    //남성 인구 비율
    @Column(nullable = false)
    private String malePpltnRate;

    //여성 인구 비율
    @Column(nullable = false)
    private String femalePpltnRate;

    //10대 인구비율
    @Column(nullable = false)
    private String ppltnRate10;

    //20대 인구비율
    @Column(nullable = false)
    private String ppltnRate20;

    //30대 인구비율
    @Column(nullable = false)
    private String ppltnRate30;

    //40대 인구비율
    @Column(nullable = false)
    private String ppltnRate40;

    //50대 인구비율
    @Column(nullable = false)
    private String ppltnRate50;

    //실시간인구 업데이트 시간
    @Column(nullable = false)
    private String ppltnTime;

    //날씨관련
    //기온
    @Column(nullable = false)
    private String temp;

    //일 최고온도
    @Column(nullable = false)
    private String maxTemp;

    //일 최저온도
    @Column(nullable = false)
    private String minTemp;

    //강수관련 메세지
    @Column(nullable = false)
    private String pcpMsg;

    //초미세먼지지표
    @Column(nullable = false)
    private String pm25Index;

    //초미세먼지농도
    @Column(nullable = false)
    private String pm25;

    //미세먼지지표
    @Column(nullable = false)
    private String pm10Index;

    //미세먼지농도
    @Column(nullable = false)
    private String pm10;

    //통합대기환경등급별 메세지
    @Column(nullable = false)
    private String airMsg;

    //날씨 업데이트 시간
    @Column(nullable = false)
    private String weatherTime;

    //소재한 자치구명
    @Column(nullable = false)
    private String guNm;

    //(자치구)확진자 수
    @Column(nullable = false)
    private String guConfirmed;

    //(자치구)확진자 수 추가
    @Column(nullable = false)
    private String guAdded;

    @Column(nullable = false)
    private String skyStts;

}
