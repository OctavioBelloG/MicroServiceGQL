// src/main/java/com/example/service/PaymentServiceImpl.java (Fragmento con rutas clave)
package com.example.service;

import com.example.dto.PaymentRequest;
import com.example.dto.PaymentResponse;
import com.example.mapper.PaymentMapper;
import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import com.example.repository.PatientRepository;
import com.example.repository.ConsultationRepository;
import com.example.repository.PaymentMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final PaymentMethodRepository methodRepository;

    @Override
    public PaymentResponse create(PaymentRequest req) {
        // 1. Validar FKs
        if (!patientRepository.existsById(req.getPatientId())) throw new EntityNotFoundException("Patient not found.");
        if (!consultationRepository.existsById(req.getConsultationId())) throw new EntityNotFoundException("Consultation not found.");
        if (!methodRepository.existsById(req.getMethodId())) throw new EntityNotFoundException("Payment Method not found.");

        // 2. Validar unicidad (Un pago por consulta)
        if (repository.existsByConsultation_ConsultationId(req.getConsultationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un pago registrado para la consulta ID: " + req.getConsultationId());
        }

        Payment saved = repository.save(PaymentMapper.toEntity(req));
        return PaymentMapper.toResponse(saved);
    }
    
    @Override
    public List<PaymentResponse> getPaymentsByPatient(Long patientId) {
        // Implementación simple
        return repository.findByPatient_PatientId(patientId).stream().map(PaymentMapper::toResponse).toList();
    }
    
    @Override
    public PaymentResponse getPaymentByConsultation(Long consultationId) {
        Payment payment = repository.findByConsultation_ConsultationId(consultationId);
        if (payment == null) {
            throw new EntityNotFoundException("Payment not found for Consultation ID: " + consultationId);
        }
        return PaymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse changeStatus(Long paymentId, String newStatus) {
        Payment existing = repository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));

        String statusUpper = newStatus.toUpperCase();
        if (!List.of("PENDING", "PAID", "CANCELLED").contains(statusUpper)) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de pago no válido.");
        }
        
        existing.setStatus(statusUpper);
        Payment saved = repository.save(existing);
        return PaymentMapper.toResponse(saved);
    }

    @Override
    public Map<String, BigDecimal> getPaymentStatistics() {
        // Mapeo de resultados de la query de estadísticas
        return repository.getPaymentStatisticsByMethod().stream()
                .collect(Collectors.toMap(
                    obj -> (String) obj[0],
                    obj -> (BigDecimal) obj[1]
                ));
    }
}