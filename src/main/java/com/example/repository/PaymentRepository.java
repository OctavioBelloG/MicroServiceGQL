// src/main/java/com/example/repository/PaymentRepository.java
package com.example.repository;

import com.example.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // RUTA: GET /payments/patient/{id}
    List<Payment> findByPatient_PatientId(Long patientId);

    // RUTA: GET /payments/consultation/{id}
    Payment findByConsultation_ConsultationId(Long consultationId);
    
    // Para validar que una consulta solo tenga un pago
    boolean existsByConsultation_ConsultationId(Long consultationId);

    // RUTA: GET /payments/statistics
    @Query("SELECT m.methodName, SUM(p.amount) FROM Payment p JOIN p.method m WHERE p.status = 'PAID' GROUP BY m.methodName")
    List<Object[]> getPaymentStatisticsByMethod();
}