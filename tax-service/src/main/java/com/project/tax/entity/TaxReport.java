package com.project.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taxReportId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal domesticTotalProfit;

    @Column(precision = 15, scale = 2)
    private BigDecimal foreignTotalProfit;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalTax;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal domesticTax;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal foreignTax;
}
