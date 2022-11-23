package com.sdy.bbbb.data.dataDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class SpotInfoDto {

    private String area_nm;
    private String ppltn_time;
    private String air_msg;
    private String area_congest_lvl;
    private String female_ppltn_rate;
    private String male_ppltn_rate;
    private String max_temp;
    private String min_temp;
    private String pcp_msg;
    private String pm10;
    private String pm10index;
    private String pm25;
    private String pm25index;
    private String ppltn_rate10;
    private String ppltn_rate20;
    private String ppltn_rate30;
    private String ppltn_rate40;
    private String ppltn_rate50;
    private String temp;
    private String sky_stts;
    private List<Map<String, String>> popByHour;
    private List<Map<String, String>> todayPopByHour;

    public SpotInfoDto(GuBaseInfo guBaseInfo, List<Map<String, String>> popByHour, List<Map<String, String>> todayPopByHour) {
        this.area_nm = guBaseInfo.getArea_nm();
        this.ppltn_time = guBaseInfo.getPpltn_time();
        this.air_msg = guBaseInfo.getAir_msg();
        this.area_congest_lvl = guBaseInfo.getArea_congest_lvl();
        this.female_ppltn_rate = guBaseInfo.getFemale_ppltn_rate();
        this.male_ppltn_rate = guBaseInfo.getMale_ppltn_rate();
        this.max_temp = guBaseInfo.getMax_temp();
        this.min_temp = guBaseInfo.getMin_temp();
        this.pcp_msg = guBaseInfo.getPcp_msg();
        this.pm10 = guBaseInfo.getPm10();
        this.pm10index = guBaseInfo.getPm10index();
        this.pm25 = guBaseInfo.getPm25();
        this.pm25index = guBaseInfo.getPm25Index();
        this.ppltn_rate10 = guBaseInfo.getPpltn_Rate10();
        this.ppltn_rate20 = guBaseInfo.getPpltn_Rate20();
        this.ppltn_rate30 = guBaseInfo.getPpltn_Rate30();
        this.ppltn_rate40 = guBaseInfo.getPpltn_Rate40();
        this.ppltn_rate50 = guBaseInfo.getPpltn_Rate50();
        this.temp = guBaseInfo.getTemp();
        this.sky_stts = guBaseInfo.getSky_Stts();
        this.popByHour = popByHour;
        this.todayPopByHour = todayPopByHour;
    }
}
