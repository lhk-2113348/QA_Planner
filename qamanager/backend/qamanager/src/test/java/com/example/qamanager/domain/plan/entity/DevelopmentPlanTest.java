package com.example.qamanager.domain.plan.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DevelopmentPlanTest {

    @Test
    void testDevelopmentPlanCreation() {
        // Given
        DevelopmentPlan plan = new DevelopmentPlan();
        LocalDate startDate = LocalDate.of(2026, 4, 1);
        LocalDate endDate = LocalDate.of(2026, 4, 15);
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        plan.setTitle("테스트 프로젝트");
        plan.setDeveloper("테스터");
        plan.setDevStartDate(startDate);
        plan.setDevEndDate(endDate);
        plan.setPurpose("테스트 목적");
        plan.setScope("테스트 범위");
        plan.setMainFeatures("테스트 기능");
        plan.setCreatedAt(createdAt);

        // Then
        assertThat(plan.getTitle()).isEqualTo("테스트 프로젝트");
        assertThat(plan.getDeveloper()).isEqualTo("테스터");
        assertThat(plan.getDevStartDate()).isEqualTo(startDate);
        assertThat(plan.getDevEndDate()).isEqualTo(endDate);
        assertThat(plan.getPurpose()).isEqualTo("테스트 목적");
        assertThat(plan.getScope()).isEqualTo("테스트 범위");
        assertThat(plan.getMainFeatures()).isEqualTo("테스트 기능");
        assertThat(plan.getCreatedAt()).isEqualTo(createdAt);
    }
}