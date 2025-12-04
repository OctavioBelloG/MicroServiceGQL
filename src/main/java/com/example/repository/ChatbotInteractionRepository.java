package com.example.repository;

import com.example.model.ChatbotInteraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotInteractionRepository extends JpaRepository<ChatbotInteraction, Long> {

    // Buscar interacciones por patient ID
    Page<ChatbotInteraction> findByPatient_PatientId(Long patientId, Pageable pageable);
}