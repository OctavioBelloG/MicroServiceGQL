package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // <--- IMPORTANTE
import com.example.model.Patient;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ❌ BORRA ESTO:
    // Page<Patient> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // ✅ AGREGA ESTO:
    // Busca coincidencias en el nombre O en el apellido paterno
    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.paternalSurname) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Patient> searchByNameOrSurname(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a")
    Page<Patient> findPatientsWithAppointments(Pageable pageable);

    Optional<Patient> findByUser_UserId(Long userId);
}