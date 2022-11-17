package com.sdy.bbbb.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepo extends JpaRepository<SpotData, Long> {

//    @Query(value = "with table1 as (select area_nm ,round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg1 from spot_data " +
//            "where ppltn_time between date_sub(now(), interval 60 minute) and now() " +
//            "group by area_nm), table2 as (select area_nm, round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg2 from spot_data " +
//            "where ppltn_time between date_sub(now(), interval 120 minute) and date_sub(now(), interval 60 minute) " +
//            "group by area_nm)\n" +
//            "select table1.area_nm, table1.ppltn_avg1, table2.ppltn_avg2, round(abs((ppltn_avg1 - table2.ppltn_avg2) / ppltn_avg1) * 100, 2) as increase_rate " +
//            "from table1 " +
//            "join table2 on table1.area_nm = table2.area_nm " +
//            "order by increase_rate desc", nativeQuery = true)
//    List findSeoulPopulation();

    @Query(value = "with table1 as (select area_nm ,round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg1 from spot_data " +
            "where ppltn_time between date_sub(now(), interval 155 minute) and date_sub(now(), interval 95 minute) " +
            "group by area_nm), table2 as (select area_nm, round(avg((area_ppltn_max + area_ppltn_min) / 2)) as ppltn_avg2 from spot_data " +
            "where ppltn_time between date_sub(now(), interval 95 minute) and date_sub(now(), interval 35 minute) " +
            "group by area_nm) " +
            "select table1.area_nm, (table2.ppltn_avg2 - table1.ppltn_avg1) as plus_minus, round(abs((table2.ppltn_avg2 - ppltn_avg1) / ppltn_avg1) * 100, 2) as increase_rate " +
            "from table1 " +
            "join table2 on table1.area_nm = table2.area_nm " +
            "order by increase_rate desc " +
            "limit 5"
            ,nativeQuery = true)
    List<PopulationDto> getPopulation();
}
