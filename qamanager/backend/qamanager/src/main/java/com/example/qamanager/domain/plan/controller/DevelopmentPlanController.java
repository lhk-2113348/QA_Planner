package com.example.qamanager.domain.plan.controller;

import com.example.qamanager.domain.plan.entity.DevelopmentPlan;
import com.example.qamanager.domain.plan.repository.DevelopmentPlanRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/development-plans")
public class DevelopmentPlanController {

    private final DevelopmentPlanRepository developmentPlanRepository;

    public DevelopmentPlanController(DevelopmentPlanRepository developmentPlanRepository) {
        this.developmentPlanRepository = developmentPlanRepository;
    }

    @GetMapping
    public List<DevelopmentPlan> getAllDevelopmentPlans() {
        return developmentPlanRepository.findAll();
    }
}