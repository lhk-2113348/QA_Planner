package com.example.qamanager.domain.plan.repository;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevelopmentPlanRepository extends JpaRepository<DevelopmentPlan, Long> {
}