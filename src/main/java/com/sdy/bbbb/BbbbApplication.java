package com.sdy.bbbb;

import com.sdy.bbbb.data.DataTagName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // scheduler 사용
@SpringBootApplication
@EnableJpaAuditing
public class BbbbApplication {
    public static void main(String[] args) {
        System.out.println(DataTagName.AREA_NM.toString().equals("AREA_NM"));
        SpringApplication.run(BbbbApplication.class, args);

    }

}
