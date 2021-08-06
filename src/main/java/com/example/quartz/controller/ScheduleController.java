package com.example.quartz.controller;

import com.example.quartz.dto.JobKeyRequest;
import com.example.quartz.dto.scheduler.ApiResponse;
import com.example.quartz.dto.scheduler.JobRequest;
import com.example.quartz.job.CronJob2;
import com.example.quartz.job.SimpleJob;
import com.example.quartz.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/scheduler")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/job", method = RequestMethod.POST)
    public ResponseEntity<?> addScheduleJob(@RequestBody JobRequest jobRequest) {
        log.info("add schedule job :: jobRequest : {}", jobRequest);
        if (jobRequest.getJobName() == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Require jobName"),
                    HttpStatus.BAD_REQUEST);
        }
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getGroupName());
        if (!scheduleService.isJobExists(jobKey)) {
            if (jobRequest.isJobTypeSimple()) {
                scheduleService.addJob(jobRequest, SimpleJob.class);
            } else {
                scheduleService.addJob(jobRequest, CronJob2.class);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job already exits"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job created successfully"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/job", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScheduleJob(@RequestBody JobKeyRequest jobKeyRequest) {
        log.info("jobKeyRequest : {}", jobKeyRequest);

        JobKey jobKey = new JobKey(jobKeyRequest.getJobName(), jobKeyRequest.getGroupName());
        if (scheduleService.isJobExists(jobKey)) {
            if (!scheduleService.isJobRunning(jobKey)) {
                scheduleService.deleteJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Job already in running state"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job does not exits"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job deleted successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateScheduleJob(@RequestBody JobRequest jobRequest) {
        log.debug("update schedule job :: jobRequest : {}", jobRequest);
        if (jobRequest.getJobName() == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Require jobName"),
                    HttpStatus.BAD_REQUEST);
        }

        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getGroupName());
        if (scheduleService.isJobExists(jobKey)) {
            if (jobRequest.isJobTypeSimple()) {
                scheduleService.updateJob(jobRequest);
            } else {
                scheduleService.updateJob(jobRequest);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job does not exits"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job updated successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllJobs() {
        //todo : 응답 값에 data, status값을 넣기
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAllJobs());
    }

    @RequestMapping(value = "/job/pause", method = RequestMethod.PUT)
    public ResponseEntity<?> pauseJob(@RequestBody JobKeyRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getGroupName());
        if (scheduleService.isJobExists(jobKey)) {
            if (!scheduleService.isJobRunning(jobKey)) {
                scheduleService.pauseJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Job already in running state"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job does not exits"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job paused successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/resume", method = RequestMethod.PUT)
    public ResponseEntity<?> resumeJob(@RequestBody JobKeyRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getGroupName());
        if (scheduleService.isJobExists(jobKey)) {
            String jobState = scheduleService.getJobState(jobKey);

            if (jobState.equals("PAUSED")) {
                scheduleService.resumeJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Job is not in paused state"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job does not exits"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job resumed successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/stop", method = RequestMethod.PUT)
    public ResponseEntity<?> stopJob(@RequestBody JobKeyRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getGroupName());
        if (scheduleService.isJobExists(jobKey)) {
            if (scheduleService.isJobRunning(jobKey)) {
                scheduleService.stopJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Job is not in running state"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job does not exits"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job stopped successfully"), HttpStatus.OK);
    }
}
