package com.example.qamanager.domain.task.controller;

import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<QaTask> getTasks() {
        return taskService.getAllTasks();
    }
}