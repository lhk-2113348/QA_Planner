package com.example.qamanager.domain.task.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class QaTaskTest {

    @Test
    void testQaTaskCreation() {
        // Given
        QaTask task = new QaTask();
        LocalDate plannedDate = LocalDate.of(2026, 4, 16);

        // When
        task.setPlanId(1L);
        task.setFeatureName("로그인 기능");
        task.setAssignee("김테스터");
        task.setPlannedDate(plannedDate);
        task.setStatus("대기중");

        // Then
        assertThat(task.getPlanId()).isEqualTo(1L);
        assertThat(task.getFeatureName()).isEqualTo("로그인 기능");
        assertThat(task.getAssignee()).isEqualTo("김테스터");
        assertThat(task.getPlannedDate()).isEqualTo(plannedDate);
        assertThat(task.getStatus()).isEqualTo("대기중");
    }

    @Test
    void testQaTaskStatusUpdate() {
        // Given
        QaTask task = new QaTask();
        task.setStatus("대기중");

        // When
        task.setStatus("진행중");

        // Then
        assertThat(task.getStatus()).isEqualTo("진행중");
    }
}