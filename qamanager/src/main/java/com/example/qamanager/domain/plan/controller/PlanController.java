package com.example.qamanager.domain.plan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plans")
public class PlanController {
    @GetMapping
    public String hello() {
        return "plan";
    }
}
