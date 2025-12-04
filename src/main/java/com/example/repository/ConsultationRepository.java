package com.example.repository;

import com.example.model.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    // Buscar consultas por ID del paciente
    @Query("SELECT c FROM Consultation c JOIN c.appointment a WHERE a.patient.patientId = :patientId")
    Page<Consultation> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);

    // Buscar consultas por ID del doctor
    @Query("SELECT c FROM Consultation c JOIN c.appointment a WHERE a.doctor.doctorId = :doctorId")
    Page<Consultation> findByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);

    // Verificar si existe consulta para un appointment
    boolean existsByAppointment_AppointmentId(Long appointmentId);
}