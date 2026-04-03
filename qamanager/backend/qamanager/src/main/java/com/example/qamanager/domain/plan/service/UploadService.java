package com.example.qamanager.domain.plan.service;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final DevelopmentPlanRepository developmentPlanRepository;
    private final QaTaskRepository qaTaskRepository;
    private static final DataFormatter DATA_FORMATTER = new DataFormatter(Locale.KOREA);

    private static final DateTimeFormatter[] SUPPORTED_DATE_FORMATS = {
        DateTimeFormatter.ISO_LOCAL_DATE,
        DateTimeFormatter.ofPattern("yyyy.MM.dd"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("yyyy-M-d"),
        DateTimeFormatter.ofPattern("yyyy.M.d"),
        DateTimeFormatter.ofPattern("yyyy/M/d")
    };

    public void upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드 파일이 비어 있습니다.");
        }

        try {
            byte[] fileBytes = file.getBytes();
            validateExcelFileSignature(fileBytes, file.getOriginalFilename());

            try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileBytes))) {
            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("엑셀 시트가 없습니다.");
            }

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
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀 파일 처리 중 오류 발생: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("엑셀 구조를 확인해주세요. 지원하지 않는 형식이 포함되어 있습니다.", e);
        }
    }

    private void validateExcelFileSignature(byte[] fileBytes, String originalFilename) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(fileBytes)) {
            FileMagic fileMagic = FileMagic.valueOf(input);
            if (fileMagic == FileMagic.OOXML || fileMagic == FileMagic.OLE2) {
                return;
            }

            String name = originalFilename == null ? "(파일명 없음)" : originalFilename;
            throw new IllegalArgumentException("지원하지 않는 엑셀 파일입니다. 일반 XLS/XLSX 형식으로 다시 저장 후 업로드해주세요. 파일명: " + name);
        } catch (IOException e) {
            throw new IllegalArgumentException("업로드 파일 형식 확인 중 오류가 발생했습니다.", e);
        }
    }

    private DevelopmentPlan parseDevelopmentPlan(Sheet sheet) {
        DevelopmentPlan plan = new DevelopmentPlan();

        if (looksLikeTemplateSheet(sheet)) {
            parseTemplateSheet(plan, sheet);
        } else {
            parseLegacyKeyValueSheet(plan, sheet);
        }

        if (plan.getTitle() == null || plan.getTitle().isBlank()) {
            throw new IllegalArgumentException("엑셀에서 제목을 찾지 못했습니다. 템플릿 형식을 확인해주세요.");
        }

        if (plan.getDeveloper() == null || plan.getDeveloper().isBlank()) {
            throw new IllegalArgumentException("엑셀에서 개발자를 찾지 못했습니다. 템플릿 형식을 확인해주세요.");
        }

        plan.setCreatedAt(LocalDateTime.now());
        return plan;
    }

    private boolean looksLikeTemplateSheet(Sheet sheet) {
        return findLabelCell(sheet, "제목") != null && findLabelCell(sheet, "개발 목적", "개발목적") != null;
    }

    private void parseTemplateSheet(DevelopmentPlan plan, Sheet sheet) {
        Cell titleLabel = findLabelCell(sheet, "제목");
        if (titleLabel != null) {
            plan.setTitle(getCellValueAsString(findRightValueCell(titleLabel)));
        }

        Cell developerLabel = findLabelCell(sheet, "개발자");
        if (developerLabel != null) {
            plan.setDeveloper(getCellValueAsString(findRightValueCell(developerLabel)));
        }

        LocalDate devDate = parseDate(findRightValueCell(findLabelCell(sheet, "개발")));
        if (devDate != null) {
            plan.setDevStartDate(devDate);
            plan.setDevEndDate(devDate);
        }

        LocalDate qaDate = parseDate(findRightValueCell(findLabelCell(sheet, "QA")));
        if (qaDate != null) {
            plan.setQaStartDate(qaDate);
            plan.setQaEndDate(qaDate);
        }

        LocalDate deployDate = parseDate(findRightValueCell(findLabelCell(sheet, "배포")));
        if (deployDate != null) {
            plan.setDeployDate(deployDate);
        }

        Cell purposeLabel = findLabelCell(sheet, "개발 목적", "개발목적");
        if (purposeLabel != null) {
            plan.setPurpose(getCellValueAsString(findRightValueCell(purposeLabel)));
        }

        Cell scopeLabel = findLabelCell(sheet, "개발 범위", "개발범위");
        if (scopeLabel != null) {
            plan.setScope(getCellValueAsString(findRightValueCell(scopeLabel)));
        }

        Cell featuresLabel = findLabelCell(sheet, "주요 기능", "주요기능");
        if (featuresLabel != null) {
            plan.setMainFeatures(getCellValueAsString(findRightValueCell(featuresLabel)));
        }
    }

    private void parseLegacyKeyValueSheet(DevelopmentPlan plan, Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 헤더 스킵

            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);

            if (cell0 == null || cell1 == null) continue;

            String key = getCellValueAsString(cell0).trim();
            String value = getCellValueAsString(cell1);

            if (key.isEmpty()) {
                continue;
            }

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
    }

    private Cell findLabelCell(Sheet sheet, String... labels) {
        int lastRow = Math.min(sheet.getLastRowNum(), 120);
        for (int rowIndex = 0; rowIndex <= lastRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            short lastCell = row.getLastCellNum();
            int maxCell = lastCell <= 0 ? 12 : Math.min(lastCell, (short) 20);

            for (int colIndex = 0; colIndex < maxCell; colIndex++) {
                Cell cell = row.getCell(colIndex);
                String normalized = normalizeLabel(getCellValueAsString(cell));
                if (normalized.isEmpty()) {
                    continue;
                }

                for (String label : labels) {
                    if (normalized.equals(normalizeLabel(label))) {
                        return cell;
                    }
                }
            }
        }

        return null;
    }

    private Cell findRightValueCell(Cell labelCell) {
        if (labelCell == null || labelCell.getRow() == null) {
            return null;
        }

        Row row = labelCell.getRow();
        int startCol = labelCell.getColumnIndex() + 1;
        int maxCol = Math.max(startCol + 1, Math.min(Math.max(row.getLastCellNum(), (short) (startCol + 8)), (short) 30));

        for (int col = startCol; col < maxCol; col++) {
            Cell cell = row.getCell(col);
            if (!getCellValueAsString(cell).isEmpty()) {
                return cell;
            }
        }

        return null;
    }

    private String normalizeLabel(String value) {
        if (value == null) {
            return "";
        }

        return value
            .replace(" ", "")
            .replace("\t", "")
            .replace("\n", "")
            .replace(":", "")
            .replace("：", "")
            .trim()
            .toUpperCase(Locale.ROOT);
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

        return DATA_FORMATTER.formatCellValue(cell).trim();
    }

    private LocalDate parseDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            String raw = getCellValueAsString(cell);
            if (raw.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : SUPPORTED_DATE_FORMATS) {
                try {
                    return LocalDate.parse(raw, formatter);
                } catch (DateTimeParseException ignored) {
                    // Try next format.
                }
            }

            throw new IllegalArgumentException("날짜 형식을 인식할 수 없습니다: " + raw);
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 파싱 오류: " + e.getMessage(), e);
        }
    }
}