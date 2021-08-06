package com.example.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        log.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}