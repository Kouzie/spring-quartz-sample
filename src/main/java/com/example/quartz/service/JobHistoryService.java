package com.example.quartz.service;

import com.example.quartz.dto.scheduler.JobRequest;
import com.example.quartz.exception.ResourceNotFoundException;
import com.example.quartz.model.JobHistory;
import com.example.quartz.model.JobStatus;
import com.example.quartz.model.StateType;
import com.example.quartz.repository.JobHistoryRepository;
import com.example.quartz.repository.JobStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service("jobHistoryService")
public class JobHistoryService {

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Autowired
    private JobStatusRepository jobStatusRepository;

    public JobHistory addJob(JobRequest jobRequest) {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setJobName(jobRequest.getJobName());
        jobHistory.setJobGroup(jobRequest.getGroupName());
        jobHistory.setJobType(jobRequest.getCurrentJobType());
        jobHistory = jobHistoryRepository.save(jobHistory);

        JobStatus jobStatus = new JobStatus();
        jobStatus.setJobState(StateType.CREATE);
        jobStatus.setJobHistory(jobHistory);
        jobStatusRepository.save(jobStatus);
        return jobHistory;
    }

    public JobStatus deleteJob(JobKey jobKey) {
        return saveJobStatus(jobKey, StateType.DELETE);
    }

    public JobStatus updateJob(JobKey jobKey) {
        return saveJobStatus(jobKey, StateType.UPDATE);
    }

    public JobStatus pauseJob(JobKey jobKey) {
        return saveJobStatus(jobKey, StateType.PAUSE);
    }

    public JobStatus resumeJob(JobKey jobKey) {
        return saveJobStatus(jobKey, StateType.RESUME);
    }

    public JobStatus stopJob(JobKey jobKey) {
        return saveJobStatus(jobKey, StateType.STOP);
    }

    /**
     * LazyInitializationException ????????? @Transactional ??????
     * https://jdm.kr/blog/141
     *
     * @param pageable
     * @return
     */
    @Transactional
    public Page<JobStatus> getAllJobs(Pageable pageable) {
        Page<JobStatus> jobStatusPage = jobStatusRepository.findAll(pageable);
//
//        List<JobHistoryStatusResponse> result = new ArrayList<>();
//        JobHistoryStatusResponse jobHistoryStatusResponse;
//
//        for (JobStatus jobStatus : jobStatusPage.getContent()) {
//            jobHistoryStatusResponse = new JobHistoryStatusResponse();
//            jobHistoryStatusResponse.setStatusId(jobStatus.getStatusId());
//            jobHistoryStatusResponse.setJobState(jobStatus.getJobState());
//            jobHistoryStatusResponse.setCreateDt(jobStatus.getCreateDt());
//            jobHistoryStatusResponse.setJobName(jobStatus.getJobHistory().getJobName());
//            jobHistoryStatusResponse.setJobGroup(jobStatus.getJobHistory().getJobGroup());
//            jobHistoryStatusResponse.setJobType(jobStatus.getJobHistory().getJobType());
//            result.add(jobHistoryStatusResponse);
//        }
        return jobStatusPage;
    }

    private JobStatus saveJobStatus(JobKey jobKey, StateType delete) {
        JobHistory jobHistory = jobHistoryRepository
                .findFirstByJobNameAndJobGroupOrderByHistoryIdDesc(jobKey.getName(), jobKey.getGroup())
                .orElseThrow(() -> new ResourceNotFoundException("jobKey " + jobKey + " not found"));
        jobHistoryRepository.save(jobHistory);

        JobStatus jobStatus = new JobStatus();
        jobStatus.setJobState(delete);
        jobStatus.setJobHistory(jobHistory);
        return jobStatusRepository.save(jobStatus);
    }
}
