package com.sdy.bbbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YghhApplication {

    public static void main(String[] args) {
        SpringApplication.run(YghhApplication.class, args);
    }

}
