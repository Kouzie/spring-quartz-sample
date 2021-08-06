package com.example.quartz.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "job_history")
public class JobHistory {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @NotNull
    @Column(name = "job_name", length = 50)
    private String jobName;

    @NotNull
    @Column(name = "job_group", length = 50)
    private String jobGroup;

    @NotNull
    @Column(name = "job_type", length = 50)
    @Enumerated(value = EnumType.STRING)
    private JobType jobType;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}
