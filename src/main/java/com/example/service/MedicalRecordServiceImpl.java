package com.example.service;

import java.util.List;
import java.sql.Timestamp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar
import com.example.model.MedicalRecord;
import com.example.dto.MedicalRecordRequest;
import com.example.dto.MedicalRecordResponse;
import com.example.mapper.MedicalRecordMapper;
import com.example.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;

    // ... (findAll y findById eliminados/modificados)
    
    // Implementación de findByPatientId
    @Override
    public MedicalRecordResponse findByPatientId(Long patientId) {
        MedicalRecord m = repository.findByPatient_PatientId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("MedicalRecord for Patient ID not found: " + patientId));
        return MedicalRecordMapper.toResponse(m);
    }
    
    // Implementación de create (Validar unicidad 1-1 por PatientId si aplica)
    @Override
    public MedicalRecordResponse create(MedicalRecordRequest req) {
        // Lógica de validación 1-1 si es necesaria
        // if (repository.findByPatient_PatientId(req.getPatientId()).isPresent()) { ... }
        MedicalRecord saved = repository.save(MedicalRecordMapper.toEntity(req));
        return MedicalRecordMapper.toResponse(saved);
    }
    
    // Implementación de update
    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest req) {
        MedicalRecord existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("MedicalRecord not found: " + id));
        MedicalRecordMapper.copyToEntity(req, existing);
        // Opcional: Actualizar el campo lastUpdated aquí si se hace un PUT completo
        existing.setLastUpdated(new Timestamp(System.currentTimeMillis())); 
        MedicalRecord saved = repository.save(existing);
        return MedicalRecordMapper.toResponse(saved);
    }
    
    // Implementación de updateLastUpdatedDate
    @Override
    public MedicalRecordResponse updateLastUpdatedDate(Long id) {
        MedicalRecord existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("MedicalRecord not found: " + id));
        existing.setLastUpdated(new Timestamp(System.currentTimeMillis())); // Actualizar solo la fecha
        MedicalRecord saved = repository.save(existing);
        return MedicalRecordMapper.toResponse(saved);
    }
    
    // Implementación de searchRecords
    @Override
    public List<MedicalRecordResponse> searchRecords(String keyword) {
        return repository.searchByKeyword(keyword).stream()
                .map(MedicalRecordMapper::toResponse)
                .toList();
    }
}