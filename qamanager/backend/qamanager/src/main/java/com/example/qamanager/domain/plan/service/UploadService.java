package com.example.qamanager.domain.plan.service;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final DevelopmentPlanRepository developmentPlanRepository;
    private final QaTaskRepository qaTaskRepository;

    public void upload(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            // 첫 번째 시트에서 개발계획 정보 읽기
            Sheet planSheet = workbook.getSheetAt(0);
            DevelopmentPlan developmentPlan = parseDevelopmentPlan(planSheet);

            // 개발계획 저장
            DevelopmentPlan savedPlan = developmentPlanRepository.save(developmentPlan);
            System.out.println("개발계획 저장 완료: " + savedPlan.getTitle());

            // QA 작업 자동 생성
            List<QaTask> qaTasks = generateQaTasks(savedPlan);
            qaTaskRepository.saveAll(qaTasks);
            System.out.println("QA 작업 " + qaTasks.size() + "개 자동 생성 완료");

        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 처리 중 오류 발생: " + e.getMessage());
        }
    }

    private DevelopmentPlan parseDevelopmentPlan(Sheet sheet) {
        DevelopmentPlan plan = new DevelopmentPlan();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 헤더 스킵

            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);

            if (cell0 == null || cell1 == null) continue;

            String key = cell0.getStringCellValue();
            String value = getCellValueAsString(cell1);

            switch (key) {
                case "프로젝트명":
                    plan.setTitle(value);
                    break;
                case "담당자":
                    plan.setDeveloper(value);
                    break;
                case "개발시작일":
                    plan.setDevStartDate(parseDate(cell1));
                    break;
                case "개발종료일":
                    plan.setDevEndDate(parseDate(cell1));
                    break;
                case "QA시작일":
                    plan.setQaStartDate(parseDate(cell1));
                    break;
                case "QA종료일":
                    plan.setQaEndDate(parseDate(cell1));
                    break;
                case "배포일":
                    plan.setDeployDate(parseDate(cell1));
                    break;
                case "목적":
                    plan.setPurpose(value);
                    break;
                case "범위":
                    plan.setScope(value);
                    break;
                case "주요기능":
                    plan.setMainFeatures(value);
                    break;
            }
        }

        plan.setCreatedAt(LocalDateTime.now());
        return plan;
    }

    private List<QaTask> generateQaTasks(DevelopmentPlan plan) {
        List<QaTask> tasks = new ArrayList<>();

        // 기본 QA 작업들 자동 생성
        String[] defaultFeatures = {
            "로그인 기능",
            "사용자 관리",
            "데이터 처리",
            "UI/UX 검증",
            "성능 테스트",
            "보안 검증"
        };

        LocalDate qaStart = plan.getQaStartDate();
        if (qaStart == null) {
            qaStart = LocalDate.now();
        }

        for (int i = 0; i < defaultFeatures.length; i++) {
            QaTask task = new QaTask();
            task.setPlanId(plan.getId());
            task.setFeatureName(defaultFeatures[i]);
            task.setAssignee(plan.getDeveloper());
            task.setPlannedDate(qaStart.plusDays(i * 2)); // 2일 간격으로 배분
            task.setStatus("대기중");

            tasks.add(task);
        }

        return tasks;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private LocalDate parseDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                // 날짜 문자열 파싱 로직 (필요시 구현)
                return null;
            }
        } catch (Exception e) {
            System.err.println("날짜 파싱 오류: " + e.getMessage());
        }
        return null;
    }
}