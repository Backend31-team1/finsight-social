package com.project.price.entity;

import com.project.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AccountId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
