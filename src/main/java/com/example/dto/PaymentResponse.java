// src/main/java/com/example/dto/PaymentResponse.java
package com.example.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Value
@Builder
public class PaymentResponse {
    Long paymentId;
    Long patientId;
    Long consultationId;
    Long methodId;
    String methodName; // Útil para respuesta
    BigDecimal amount;
    String status;
    Timestamp paymentDate;
}