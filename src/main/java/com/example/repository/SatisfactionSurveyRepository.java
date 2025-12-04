// src/main/java/com/example/repository/SatisfactionSurveyRepository.java
package com.example.repository;

import com.example.model.SatisfactionSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SatisfactionSurveyRepository extends JpaRepository<SatisfactionSurvey, Long> {
    
    // RUTA: GET /surveys/consultation/{id}
    SatisfactionSurvey findByConsultation_ConsultationId(Long consultationId);
    
    // Validar unicidad por consulta
    boolean existsByConsultation_ConsultationId(Long consultationId);

    // RUTA: GET /surveys/doctor/{id} - Búsqueda por Doctor ID a través de Appointment
    @Query("SELECT s FROM SatisfactionSurvey s " +
           "JOIN s.consultation c JOIN c.appointment a " +
           "WHERE a.doctor.doctorId = :doctorId")
    List<SatisfactionSurvey> findByDoctorId(Long doctorId);

    // RUTA: GET /surveys/statistics - Calificación promedio por doctor
    @Query("SELECT a.doctor.fullName, AVG(s.rating) FROM SatisfactionSurvey s " +
           "JOIN s.consultation c JOIN c.appointment a " +
           "GROUP BY a.doctor.fullName ORDER BY AVG(s.rating) DESC")
    List<Object[]> getAverageRatingByDoctor();
}