package com.example.service;

import com.example.dto.ConsultationRequest;
import com.example.dto.ConsultationResponse;
import com.example.mapper.ConsultationMapper;
import com.example.model.Appointment;
import com.example.model.Consultation;
import com.example.repository.AppointmentRepository;
import com.example.repository.ConsultationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository repository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<ConsultationResponse> findAll() {
        return repository.findAll().stream()
                .map(consultation -> {
                    // Inicializar relaciones lazy
                    if (consultation.getAppointment() != null) {
                        Hibernate.initialize(consultation.getAppointment());
                        Hibernate.initialize(consultation.getAppointment().getPatient());
                        Hibernate.initialize(consultation.getAppointment().getDoctor());
                    }
                    return ConsultationMapper.toResponse(consultation);
                })
                .toList();
    }

    @Override
    public List<ConsultationResponse> findAllPaginated(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Consultation> consultations = repository.findAll(pageReq);

        return consultations.getContent().stream()
                .map(consultation -> {
                    if (consultation.getAppointment() != null) {
                        Hibernate.initialize(consultation.getAppointment());
                        Hibernate.initialize(consultation.getAppointment().getPatient());
                        Hibernate.initialize(consultation.getAppointment().getDoctor());
                    }
                    return ConsultationMapper.toResponse(consultation);
                })
                .toList();
    }

    @Override
    public ConsultationResponse findById(Long consultationId) {
        Consultation consultation = repository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada con ID: " + consultationId));

        // Inicializar relaciones lazy
        if (consultation.getAppointment() != null) {
            Hibernate.initialize(consultation.getAppointment());
            Hibernate.initialize(consultation.getAppointment().getPatient());
            Hibernate.initialize(consultation.getAppointment().getDoctor());
        }

        return ConsultationMapper.toResponse(consultation);
    }

    @Override
    public List<ConsultationResponse> findByPatientId(Long patientId, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Consultation> consultations = repository.findByPatientId(patientId, pageReq);

        return consultations.getContent().stream()
                .map(consultation -> {
                    if (consultation.getAppointment() != null) {
                        Hibernate.initialize(consultation.getAppointment());
                        Hibernate.initialize(consultation.getAppointment().getPatient());
                        Hibernate.initialize(consultation.getAppointment().getDoctor());
                    }
                    return ConsultationMapper.toResponse(consultation);
                })
                .toList();
    }

    @Override
    public List<ConsultationResponse> findByDoctorId(Long doctorId, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Consultation> consultations = repository.findByDoctorId(doctorId, pageReq);

        return consultations.getContent().stream()
                .map(consultation -> {
                    if (consultation.getAppointment() != null) {
                        Hibernate.initialize(consultation.getAppointment());
                        Hibernate.initialize(consultation.getAppointment().getPatient());
                        Hibernate.initialize(consultation.getAppointment().getDoctor());
                    }
                    return ConsultationMapper.toResponse(consultation);
                })
                .toList();
    }

    @Override
    public ConsultationResponse create(ConsultationRequest req) {
        // Validar que el appointment existe
        Appointment appointment = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + req.getAppointmentId()));

        // Validar que no exista ya una consulta para esta cita
        if (repository.existsByAppointment_AppointmentId(req.getAppointmentId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe una consulta registrada para esta cita"
            );
        }

        Consultation consultationToSave = ConsultationMapper.toEntity(req);
        consultationToSave.setAppointment(appointment);

        Consultation saved = repository.save(consultationToSave);

        // Inicializar para el response
        Hibernate.initialize(saved.getAppointment());
        Hibernate.initialize(saved.getAppointment().getPatient());
        Hibernate.initialize(saved.getAppointment().getDoctor());

        return ConsultationMapper.toResponse(saved);
    }

    @Override
    public ConsultationResponse update(Long consultationId, ConsultationRequest req) {
        Consultation existing = repository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada con ID: " + consultationId));

        // Validar si se está cambiando el appointment
        if (!existing.getAppointment().getAppointmentId().equals(req.getAppointmentId())) {
            Appointment newAppointment = appointmentRepository.findById(req.getAppointmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + req.getAppointmentId()));

            // Validar que el nuevo appointment no tenga ya una consulta
            if (repository.existsByAppointment_AppointmentId(req.getAppointmentId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La cita seleccionada ya tiene una consulta registrada"
                );
            }

            existing.setAppointment(newAppointment);
        }

        ConsultationMapper.copyToEntity(req, existing);
        Consultation saved = repository.save(existing);

        // Inicializar para el response
        Hibernate.initialize(saved.getAppointment());
        Hibernate.initialize(saved.getAppointment().getPatient());
        Hibernate.initialize(saved.getAppointment().getDoctor());

        return ConsultationMapper.toResponse(saved);
    }
}