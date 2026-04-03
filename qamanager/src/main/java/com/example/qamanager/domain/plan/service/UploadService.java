package com.example.qamanager.domain.plan.service;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final DevelopmentPlanRepository developmentPlanRepository;
    private final QaTaskRepository qaTaskRepository;

}