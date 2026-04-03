package com.example.qamanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "QA Manager API");
        response.put("status", "Running");
        response.put("database", "PostgreSQL Connected");
        response.put("endpoints", "/plans, /tasks, /development-plans");
        return response;
    }
}