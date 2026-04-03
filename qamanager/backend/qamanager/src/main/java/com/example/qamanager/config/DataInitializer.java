package com.example.qamanager.config;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.entity.Plan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import com.example.qamanager.domain.plan.repository.PlanRepository;
import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.entity.Task;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import com.example.qamanager.domain.task.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PlanRepository planRepository,
                                  TaskRepository taskRepository,
                                  DevelopmentPlanRepository developmentPlanRepository,
                                  QaTaskRepository qaTaskRepository) {
        return args -> {
            // Create sample Plan
            Plan plan = new Plan();
            plan.setTitle("QA 프로젝트 계획");
            planRepository.save(plan);

            // Create sample Task
            Task task = new Task();
            task.setDescription("단위 테스트 작성");
            taskRepository.save(task);

            // Create sample DevelopmentPlan
            DevelopmentPlan devPlan = new DevelopmentPlan();
            devPlan.setTitle("모바일 앱 개발 계획");
            devPlan.setDeveloper("김개발");
            devPlan.setDevStartDate(LocalDate.of(2026, 4, 1));
            devPlan.setDevEndDate(LocalDate.of(2026, 4, 15));
            devPlan.setQaStartDate(LocalDate.of(2026, 4, 16));
            devPlan.setQaEndDate(LocalDate.of(2026, 4, 30));
            devPlan.setDeployDate(LocalDate.of(2026, 5, 1));
            devPlan.setPurpose("사용자 경험 개선을 위한 모바일 앱 개발");
            devPlan.setScope("안드로이드 및 iOS 앱 개발, API 연동");
            devPlan.setMainFeatures("로그인, 프로필 관리, 데이터 동기화");
            devPlan.setCreatedAt(LocalDateTime.now());
            developmentPlanRepository.save(devPlan);

            // Create sample QaTask
            QaTask qaTask = new QaTask();
            qaTask.setPlanId(1L);
            qaTask.setFeatureName("사용자 로그인 기능");
            qaTask.setAssignee("김테스터");
            qaTask.setPlannedDate(LocalDate.of(2026, 4, 16));
            qaTask.setStatus("대기중");
            qaTaskRepository.save(qaTask);

            System.out.println("샘플 데이터가 생성되었습니다!");
        };
    }
}