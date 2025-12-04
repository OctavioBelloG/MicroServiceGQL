package com.example.service;

import com.example.dto.ChatbotInteractionRequest;
import com.example.dto.ChatbotInteractionResponse;
import com.example.mapper.ChatbotInteractionMapper;
import com.example.model.ChatbotInteraction;
import com.example.model.Patient;
import com.example.repository.ChatbotInteractionRepository;
import com.example.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatbotInteractionServiceImpl implements ChatbotInteractionService {

    private final ChatbotInteractionRepository repository;
    private final PatientRepository patientRepository;

    @Override
    public List<ChatbotInteractionResponse> findByPatientId(Long patientId, int page, int pageSize) {
        // Validar que el paciente existe
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("Paciente no encontrado con ID: " + patientId);
        }

        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<ChatbotInteraction> interactions = repository.findByPatient_PatientId(patientId, pageReq);

        return interactions.getContent().stream()
                .map(interaction -> {
                    Hibernate.initialize(interaction.getPatient());
                    return ChatbotInteractionMapper.toResponse(interaction);
                })
                .toList();
    }

    @Override
    public ChatbotInteractionResponse findById(Long chatbotId) {
        ChatbotInteraction interaction = repository.findById(chatbotId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Interacción no encontrada con ID: " + chatbotId));

        Hibernate.initialize(interaction.getPatient());
        return ChatbotInteractionMapper.toResponse(interaction);
    }

    @Override
    public ChatbotInteractionResponse create(ChatbotInteractionRequest req) {
        // Validar que el paciente existe
        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Paciente no encontrado con ID: " + req.getPatientId()));

        ChatbotInteraction interactionToSave = ChatbotInteractionMapper.toEntity(req);
        interactionToSave.setPatient(patient);

        ChatbotInteraction saved = repository.save(interactionToSave);
        Hibernate.initialize(saved.getPatient());

        return ChatbotInteractionMapper.toResponse(saved);
    }

    @Override
    public ChatbotInteractionResponse update(Long chatbotId, ChatbotInteractionRequest req) {
        ChatbotInteraction existing = repository.findById(chatbotId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Interacción no encontrada con ID: " + chatbotId));

        // Si se está cambiando el paciente
        if (!existing.getPatient().getPatientId().equals(req.getPatientId())) {
            Patient newPatient = patientRepository.findById(req.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Paciente no encontrado con ID: " + req.getPatientId()));
            existing.setPatient(newPatient);
        }

        ChatbotInteractionMapper.copyToEntity(req, existing);
        ChatbotInteraction saved = repository.save(existing);

        Hibernate.initialize(saved.getPatient());
        return ChatbotInteractionMapper.toResponse(saved);
    }
}