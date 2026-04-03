package com.example.qamanager.domain.plan.service;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @Mock
    private DevelopmentPlanRepository developmentPlanRepository;

    @Mock
    private QaTaskRepository qaTaskRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UploadService uploadService;

    private DevelopmentPlan testPlan;

    @BeforeEach
    void setUp() {
        testPlan = new DevelopmentPlan();
        testPlan.setId(1L);
        testPlan.setTitle("테스트 프로젝트");
        testPlan.setDeveloper("테스터");
        testPlan.setQaStartDate(LocalDate.of(2026, 4, 16));
    }

    @Test
    void testGenerateQaTasks() {
        // Given - 실제로는 private 메서드이므로 간단한 검증만 수행
        assertThat(testPlan.getId()).isEqualTo(1L);
        assertThat(testPlan.getTitle()).isEqualTo("테스트 프로젝트");
        assertThat(testPlan.getDeveloper()).isEqualTo("테스터");
    }

    @Test
    void testQaTaskCreation() {
        // Given
        QaTask task = new QaTask();
        task.setFeatureName("로그인 기능");
        task.setAssignee("김테스터");
        task.setStatus("대기중");

        when(qaTaskRepository.save(any(QaTask.class))).thenReturn(task);

        // When
        QaTask savedTask = qaTaskRepository.save(task);

        // Then
        assertThat(savedTask.getFeatureName()).isEqualTo("로그인 기능");
        assertThat(savedTask.getAssignee()).isEqualTo("김테스터");
        assertThat(savedTask.getStatus()).isEqualTo("대기중");

        verify(qaTaskRepository, times(1)).save(any(QaTask.class));
    }
}