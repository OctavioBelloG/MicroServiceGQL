package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional; // Importar
import java.util.List; // Importar
import com.example.model.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
// NUEVO: GET /records/{patientId} -> Buscar expediente por ID de paciente
    Optional<MedicalRecord> findByPatient_PatientId(Long patientId);

    // NUEVO: GET /records/search?keyword= -> Búsqueda por palabra
    @Query("SELECT r FROM MedicalRecord r WHERE " +
           "LOWER(r.allergies) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.vaccines) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.medicalHistory) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MedicalRecord> searchByKeyword(String keyword);
}
