package com.example.qamanager.domain.plan.repository;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DevelopmentPlanRepositoryTest {

    @Autowired
    private DevelopmentPlanRepository repository;

    @Test
    void testSaveAndFindDevelopmentPlan() {
        // Given
        DevelopmentPlan plan = new DevelopmentPlan();
        plan.setTitle("테스트 개발계획");
        plan.setDeveloper("테스터");
        plan.setDevStartDate(LocalDate.of(2026, 4, 1));
        plan.setDevEndDate(LocalDate.of(2026, 4, 15));
        plan.setPurpose("테스트 목적");
        plan.setScope("테스트 범위");
        plan.setMainFeatures("테스트 기능");
        plan.setCreatedAt(LocalDateTime.now());

        // When
        DevelopmentPlan savedPlan = repository.save(plan);

        // Then
        assertThat(savedPlan.getId()).isNotNull();
        assertThat(savedPlan.getTitle()).isEqualTo("테스트 개발계획");
        assertThat(savedPlan.getDeveloper()).isEqualTo("테스터");
    }

    @Test
    void testFindAll() {
        // Given
        DevelopmentPlan plan1 = new DevelopmentPlan();
        plan1.setTitle("프로젝트 A");
        plan1.setDeveloper("개발자A");
        plan1.setCreatedAt(LocalDateTime.now());

        DevelopmentPlan plan2 = new DevelopmentPlan();
        plan2.setTitle("프로젝트 B");
        plan2.setDeveloper("개발자B");
        plan2.setCreatedAt(LocalDateTime.now());

        repository.save(plan1);
        repository.save(plan2);

        // When
        List<DevelopmentPlan> plans = repository.findAll();

        // Then
        assertThat(plans).hasSizeGreaterThanOrEqualTo(2);
        assertThat(plans.stream().anyMatch(p -> p.getTitle().equals("프로젝트 A"))).isTrue();
        assertThat(plans.stream().anyMatch(p -> p.getTitle().equals("프로젝트 B"))).isTrue();
    }

    @Test
    void testFindById() {
        // Given
        DevelopmentPlan plan = new DevelopmentPlan();
        plan.setTitle("찾을 프로젝트");
        plan.setDeveloper("테스터");
        plan.setCreatedAt(LocalDateTime.now());

        DevelopmentPlan savedPlan = repository.save(plan);

        // When
        Optional<DevelopmentPlan> foundPlan = repository.findById(savedPlan.getId());

        // Then
        assertThat(foundPlan).isPresent();
        assertThat(foundPlan.get().getTitle()).isEqualTo("찾을 프로젝트");
        assertThat(foundPlan.get().getDeveloper()).isEqualTo("테스터");
    }
}