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

    public void upload(MultipartFile file) {
        // TODO: Excel 파일 처리 로직 구현
        System.out.println("파일 업로드 처리: " + file.getOriginalFilename());
    }
}