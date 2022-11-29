package com.sdy.bbbb.data;

import com.sdy.bbbb.data.dataDto.GuBaseInfo;
import com.sdy.bbbb.data.dataDto.JamDto;
import com.sdy.bbbb.data.dataDto.PopulationDto;
import com.sdy.bbbb.data.dataDto.SpotCalculated;
import org.geolatte.geom.M;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataRepository extends JpaRepository<SpotData, Long> {

    // 데이터 2번
//    @Query(value = "with table1 as (select gu_nm, area_nm ,round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg1 from spot_data " +
//            "where ppltn_time between date_sub(now(), interval 155 minute) and date_sub(now(), interval 95 minute) " +
//            "group by area_nm), table2 as (select area_congest_lvl, area_nm, round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg2 from spot_data " +
//            "where ppltn_time between date_sub(now(), interval 95 minute) and date_sub(now(), interval 35 minute) " +
//            "group by area_nm) " +
//            "select row_number() over(order by round(abs((table2.ppltn_avg2 - ppltn_avg1) / ppltn_avg1) * 100, 2) desc) as row_num, "+
//            "table2.area_congest_lvl, table1.gu_nm, table1.area_nm, (table2.ppltn_avg2 - table1.ppltn_avg1) as plus_minus, "+
//            "round(abs((table2.ppltn_avg2 - ppltn_avg1) / ppltn_avg1) * 100, 2) as increase_rate " +
//            "from table1 " +
//            "join table2 on table1.area_nm = table2.area_nm " +
//            "order by increase_rate desc " +
//            "limit 5",
//            nativeQuery = true)
//    List<PopulationDto> getPopulationFromDb();

    // 데이터 2-1번
    @Query(value = "with table1 as (select gu_nm, area_nm ,round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg1 from (select * from spot_data " +
            "order by id desc limit 1176, 588 ) as s1 " +
            "group by area_nm), table2 as (select area_congest_lvl, area_nm, round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg2 from (select * from spot_data " +
            "order by id desc limit 1176, 588) as s1 " +
            "group by area_nm) " +
            "select row_number() over(order by round(abs((table2.ppltn_avg2 - ppltn_avg1) / ppltn_avg1) * 100, 2) desc) as row_num, "+
            "table2.area_congest_lvl, table1.gu_nm, table1.area_nm, (table2.ppltn_avg2 - table1.ppltn_avg1) as plus_minus, "+
            "round(abs((table2.ppltn_avg2 - ppltn_avg1) / ppltn_avg1) * 100, 2) as increase_rate " +
            "from table1 " +
            "join table2 on table1.area_nm = table2.area_nm " +
            "order by increase_rate desc " +
            "limit 5",
            nativeQuery = true)
    List<PopulationDto> getPopulationFromDb();

    // 데이터 1번 주말 데이터
    @Query(value = "select week(now()) as ww, a.area_nm, a.gu_nm, sum(a.score1) as score_sum " +
            "from ( " +
            "select area_nm, gu_nm, " +
            "case " +
            "when area_congest_lvl = '여유' then 1 " +
            "when area_congest_lvl = '보통' then 3 " +
            "when area_congest_lvl = '붐빔' then 6 " +
            "when area_congest_lvl = '매우 붐빔' then 10 " +
            "end as 'score1' " +
            "from spot_data sd " +
            "where ppltn_time between date_format(date_sub(now(), interval 2 day), '%Y-%m-$d 00:00:00') and date_format(date_sub(now(), interval 1 day), '%Y-%m-%d 23:59:59') " +
            ") as a " +
            "group by area_nm " +
            "order by score_sum desc " +
            "limit 3",
            nativeQuery = true)
    List<JamDto> getJamWeekendFromDb();

    // 데이터 1번 주중 데이터
    @Query(value = "select week(now()) as ww, a.area_nm, a.gu_nm, sum(a.score1) as score_sum " +
            "from ( " +
            "select area_nm, gu_nm, " +
            "case " +
            "when area_congest_lvl = '여유' then 1 " +
            "when area_congest_lvl = '보통' then 3 " +
            "when area_congest_lvl = '붐빔' then 6 " +
            "when area_congest_lvl = '매우 붐빔' then 10 " +
            "end as 'score1' " +
            "from spot_data sd " +
            "where ppltn_time between date_format(date_sub(now(), interval 5 day), '%Y-%m-$d 00:00:00') and date_format(date_sub(now(), interval 1 day), '%Y-%m-%d 23:59:59') " +
            ") as a " +
            "group by area_nm " +
            "order by score_sum desc " +
            "limit 3",
            nativeQuery = true)
    List<JamDto> getJamWeekDayFromDb();

    // 데이터 3번
    @Query(value = "select b.gu_nm, dayofweek(ppltn_time) as day_of_week, date(b.ppltn_time) as year_month_date, Hour(ppltn_time) as that_hour, b.area_nm, round(avg(b.ingu_avg)) as population_by_hour from " +
            "(select gu_nm, area_nm, ppltn_time, (area_ppltn_max+area_ppltn_min)/2 as ingu_avg from spot_data " +
            "where substr(ppltn_time, 1,10) = date_format(date_sub(now(), interval 1 day), '%Y-%m-%d') " +
            "and gu_nm = :gu " +
            ") as b " +
            "group by area_nm, that_hour",
            nativeQuery = true)
    List<SpotCalculated> getGuInfo(String gu);

    // 데이터 3번 오늘
    @Query(value = "select b.gu_nm, dayofweek(ppltn_time) as day_of_week, date(b.ppltn_time) as year_month_date, Hour(ppltn_time) as that_hour, b.area_nm, round(avg(b.ingu_avg)) as population_by_hour from " +
            "(select gu_nm, area_nm, ppltn_time, (area_ppltn_max+area_ppltn_min)/2 as ingu_avg from spot_data " +
            "where substr(ppltn_time, 1,10) = date_format(now(), '%Y-%m-%d') " +
            "and gu_nm = :gu " +
            ") as b " +
            "group by area_nm, that_hour",
            nativeQuery = true)
    List<SpotCalculated> getGuInfoToday(String gu);

//    // 데이터 3번 기본 정보
//    @Query(value = "select ppltn_time, gu_nm, area_nm, gu_added, gu_confirmed, air_msg, area_congest_lvl, female_ppltn_rate, male_ppltn_rate, max_temp, min_temp, pcp_msg, pm10, pm10index, pm25, pm25index, ppltn_rate10, ppltn_rate20, ppltn_rate30, ppltn_rate40, ppltn_rate50, temp, sky_stts " +
//            "from spot_data " +
//            "where weather_time between date_sub(now(), interval 30 minute) and now() " +
//            "and gu_nm = :gu " +
//            "group by area_nm " +
//            "order by weather_time desc",
//            nativeQuery = true)
//    List<GuBaseInfo> getGuBaseInfo(String gu);

    // 데이터 3번 기본 정보 (데이터 안들어올 때)
    @Query(value = "select ppltn_time, gu_nm, area_nm, gu_added, gu_confirmed, air_msg, area_congest_lvl, female_ppltn_rate, male_ppltn_rate, max_temp, min_temp, pcp_msg, pm10, pm10index, pm25, pm25index, ppltn_rate10, ppltn_rate20, ppltn_rate30, ppltn_rate40, ppltn_rate50, temp, sky_stts " +
            "from spot_data " +
            "where weather_time between date_sub(now(), interval 30 minute) and now() " +
            "and gu_nm = :gu " +
            "group by area_nm " +
            "order by weather_time desc",
            nativeQuery = true)
    List<GuBaseInfo> getGuBaseInfo(String gu);


    // 데이터 삭제 로직
    @Modifying
    @Query(value = "delete from spot_data " +
            "where created_at < date_sub(now(), interval 9 day)",
            nativeQuery = true)
    int clearDb();


}
