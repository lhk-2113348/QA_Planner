package com.example.qamanager.domain.task.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QaTaskDto {
    private Long id;
    private Long planId;
    private String featureName;
    private String assignee;
    private LocalDate plannedDate;
    private String status;
}