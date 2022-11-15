package com.sdy.bbbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // scheduler 사용
@SpringBootApplication
@EnableJpaAuditing
public class BbbbApplication {
    public static void main(String[] args) {

        SpringApplication.run(BbbbApplication.class, args);

    }

}
