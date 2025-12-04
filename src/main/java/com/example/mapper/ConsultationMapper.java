package com.example.mapper;

import com.example.dto.ConsultationRequest;
import com.example.dto.ConsultationResponse;
import com.example.model.Appointment;
import com.example.model.Consultation;

import java.time.LocalDateTime;

public final class ConsultationMapper {

    private ConsultationMapper() {}

    public static ConsultationResponse toResponse(Consultation consultation) {
        if (consultation == null) return null;

        return ConsultationResponse.builder()
                .consultationId(consultation.getConsultationId())
                .appointmentId(consultation.getAppointment() != null ?
                        consultation.getAppointment().getAppointmentId() : null)
                .patientName(consultation.getAppointment() != null &&
                        consultation.getAppointment().getPatient() != null ?
                        consultation.getAppointment().getPatient().getFirstName() + " " +
                                consultation.getAppointment().getPatient().getPaternalSurname() : null)
                .doctorName(consultation.getAppointment() != null &&
                        consultation.getAppointment().getDoctor() != null ?
                        consultation.getAppointment().getDoctor().getFullName() : null)
                .diagnosis(consultation.getDiagnosis())
                .treatment(consultation.getTreatment())
                .observations(consultation.getObservations())
                .consultationDate(consultation.getConsultationDate())
                .build();
    }

    public static Consultation toEntity(ConsultationRequest dto) {
        if (dto == null) return null;

        Consultation consultation = new Consultation();

        if (dto.getAppointmentId() != null) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(dto.getAppointmentId());
            consultation.setAppointment(appointment);
        }

        consultation.setDiagnosis(dto.getDiagnosis());
        consultation.setTreatment(dto.getTreatment());
        consultation.setObservations(dto.getObservations());
        consultation.setConsultationDate(LocalDateTime.now());

        return consultation;
    }

    public static void copyToEntity(ConsultationRequest dto, Consultation entity) {
        if (dto == null || entity == null) return;

        if (dto.getAppointmentId() != null) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(dto.getAppointmentId());
            entity.setAppointment(appointment);
        }

        entity.setDiagnosis(dto.getDiagnosis());
        entity.setTreatment(dto.getTreatment());
        entity.setObservations(dto.getObservations());
    }
}