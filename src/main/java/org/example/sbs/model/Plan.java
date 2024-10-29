package org.example.sbs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.sbs.enums.BillingCycle;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle billingCycle;

    @Column(name = "max_users", nullable = false)
    private Long maxUsers;
}
