package com.example.qamanager.domain.plan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DevelopmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String developer;

    private LocalDate devStartDate;
    private LocalDate devEndDate;

    private LocalDate qaStartDate;
    private LocalDate qaEndDate;

    private LocalDate deployDate;

    @Lob
    private String purpose;

    @Lob
    private String scope;

    @Lob
    private String mainFeatures;

    private LocalDateTime createdAt;
}
