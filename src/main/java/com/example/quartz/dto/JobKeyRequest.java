package com.example.quartz.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobKeyRequest {
    private String jobName;
    private String groupName;
}
