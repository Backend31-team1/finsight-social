package com.project.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
public class ProfitItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assetId;
    private Long userId;
    private Long transactionId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
