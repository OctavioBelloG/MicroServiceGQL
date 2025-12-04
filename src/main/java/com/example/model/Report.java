// src/main/java/com/example/model/Report.java
package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "REPORTS")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_report_id")
    private Long reportId;

    @Column(name = "report_type", nullable = false)
    private String reportType; // usage, trends, satisfaction, finance

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by") // REFERENCES USERS(pk_user_id) ON DELETE SET NULL
    private User generatedBy;

    @Column(name = "generated_at")
    private Timestamp generatedAt;

    // Mapeo de JSONB como String/TEXT en JPA simple
    @Column(name = "data", columnDefinition = "JSONB")
    private String data; 
}