package com.example.quartz.schedule;

import com.example.quartz.schedule.job.HelloJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class QuartzTestConfig {

    private final SchedulerFactoryBean schedulerFactory;

    @PostConstruct
    public void scheduled() throws SchedulerException {
        JobDataMap map1 = new JobDataMap(Collections.singletonMap("num", 1));
        JobDataMap map2 = new JobDataMap(Collections.singletonMap("num", 2));
        JobDetail job1 = jobDetail("hello1", "hello-group", map1);
        JobDetail job2 = jobDetail("hello2", "hello-group", map2);
        SimpleTrigger trigger1 = trigger("trigger1", "trigger-group");
        SimpleTrigger trigger2 = trigger("trigger2", "trigger-group");
        schedulerFactory.getObject().scheduleJob(job1, trigger1);
        schedulerFactory.getObject().scheduleJob(job2, trigger2);
    }

    public JobDetail jobDetail(String name, String group, JobDataMap dataMap) {
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity(name, group)
                .withDescription("simple hello job")
                .usingJobData(dataMap)
                .build();
        return job;
    }

    public SimpleTrigger trigger(String name, String group) {
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
                .withDescription("hello my trigger")
                .build();
        return trigger;
    }
}
