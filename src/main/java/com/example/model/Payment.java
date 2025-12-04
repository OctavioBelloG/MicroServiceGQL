// src/main/java/com/example/model/Payment.java
package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "PAYMENTS")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "fk_consultation_id", nullable = false, unique = true) // 1 pago por 1 consulta
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_method_id")
    private PaymentMethod method;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false) // pending, paid, cancelled
    private String status;

    @Column(name = "payment_date")
    private Timestamp paymentDate;
}