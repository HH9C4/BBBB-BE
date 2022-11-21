package com.sdy.bbbb.data;

import com.sdy.bbbb.data.dataDto.JamDto;
import com.sdy.bbbb.data.dataDto.PopulationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataRepository extends JpaRepository<SpotData, Long> {

    // 데이터 2번
    @Query(value = "with table1 as (select gu_nm, area_nm ,round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg1 from spot_data " +
            "where ppltn_time between date_sub(now(), interval 155 minute) and date_sub(now(), interval 95 minute) " +
            "group by area_nm), table2 as (select area_congest_lvl, area_nm, round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg2 from spot_data " +
            "where ppltn_time between date_sub(now(), interval 95 minute) and date_sub(now(), interval 35 minute) " +
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

    // 데이터 1번
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
            "limit 5",
            nativeQuery = true)
    List<JamDto> getJamWeekendFromDb();


}
