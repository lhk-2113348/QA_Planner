package com.example.qamanager.domain.task.controller;

import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.service.QaTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qa-tasks")
@RequiredArgsConstructor
public class QaTaskController {

    private final QaTaskService qaTaskService;

    @GetMapping
    public List<QaTask> getAllQaTasks() {
        return qaTaskService.getAllQaTasks();
    }

    @PostMapping
    public QaTask createQaTask(@RequestBody QaTask qaTask) {
        return qaTaskService.saveQaTask(qaTask);
    }
}