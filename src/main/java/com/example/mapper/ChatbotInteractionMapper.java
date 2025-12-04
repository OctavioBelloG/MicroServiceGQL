package com.example.mapper;

import com.example.dto.ChatbotInteractionRequest;
import com.example.dto.ChatbotInteractionResponse;
import com.example.model.ChatbotInteraction;
import com.example.model.Patient;

import java.time.LocalDateTime;

public final class ChatbotInteractionMapper {

    private ChatbotInteractionMapper() {}

    public static ChatbotInteractionResponse toResponse(ChatbotInteraction interaction) {
        if (interaction == null) return null;

        return ChatbotInteractionResponse.builder()
                .chatbotId(interaction.getChatbotId())
                .patientId(interaction.getPatient() != null ? interaction.getPatient().getPatientId() : null)
                .patientName(interaction.getPatient() != null ?
                        interaction.getPatient().getFirstName() + " " +
                                interaction.getPatient().getPaternalSurname() : null)
                .reportedSymptoms(interaction.getReportedSymptoms())
                .recommendation(interaction.getRecommendation())
                .interactionDate(interaction.getInteractionDate())
                .build();
    }

    public static ChatbotInteraction toEntity(ChatbotInteractionRequest dto) {
        if (dto == null) return null;

        ChatbotInteraction interaction = new ChatbotInteraction();

        if (dto.getPatientId() != null) {
            Patient patient = new Patient();
            patient.setPatientId(dto.getPatientId());
            interaction.setPatient(patient);
        }

        interaction.setReportedSymptoms(dto.getReportedSymptoms());
        interaction.setRecommendation(dto.getRecommendation());
        interaction.setInteractionDate(LocalDateTime.now());

        return interaction;
    }

    public static void copyToEntity(ChatbotInteractionRequest dto, ChatbotInteraction entity) {
        if (dto == null || entity == null) return;

        if (dto.getPatientId() != null) {
            Patient patient = new Patient();
            patient.setPatientId(dto.getPatientId());
            entity.setPatient(patient);
        }

        entity.setReportedSymptoms(dto.getReportedSymptoms());
        entity.setRecommendation(dto.getRecommendation());
    }
}