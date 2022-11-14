package com.sdy.bbbb.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    @Scheduled(cron="*/3 * * * * *")
    public void scheduleRun(){
        System.out.println("얄루");
    }

}