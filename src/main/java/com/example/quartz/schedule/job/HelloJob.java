package com.example.quartz.schedule.job;

import com.example.quartz.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
public class HelloJob implements Job {
    private final TestService service;

//    @Override
//    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//
//    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        service.test();
        log.info("RequestContractJob execute invoked, job-detail-key:{}, fired-time:{}", context.getJobDetail().getKey(), context.getFireTime());
        log.info("RequestContractJob execute complete");
    }
}
