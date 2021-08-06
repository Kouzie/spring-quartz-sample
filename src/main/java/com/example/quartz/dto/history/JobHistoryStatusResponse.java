package com.example.quartz.dto.history;

import com.example.quartz.model.JobType;
import com.example.quartz.model.StateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class JobHistoryStatusResponse {
    private Long statusId;
    private StateType jobState;
    private Date createDt;

    private String jobName;
    private String jobGroup;
    private JobType jobType;
}
