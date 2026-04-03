package com.example.qamanager.domain.task.repository;

import com.example.qamanager.domain.task.entity.QaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QaTaskRepository extends JpaRepository<QaTask, Long> {
    List<QaTask> findAllByOrderByPlannedDateAsc();
}