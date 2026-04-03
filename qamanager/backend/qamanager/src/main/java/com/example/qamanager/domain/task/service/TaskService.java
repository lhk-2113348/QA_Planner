package com.example.qamanager.domain.task.service;

import com.example.qamanager.domain.task.entity.QaTask;
import com.example.qamanager.domain.task.repository.QaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final QaTaskRepository qaTaskRepository;

    public List<QaTask> getAllTasks() {
        return qaTaskRepository.findAllByOrderByPlannedDateAsc();
    }
}
