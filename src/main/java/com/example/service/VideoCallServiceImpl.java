package com.example.service;

import com.example.dto.VideoCallRequest;
import com.example.dto.VideoCallResponse;
import com.example.mapper.VideoCallMapper;
import com.example.model.Appointment;
import com.example.model.VideoCall;
import com.example.repository.AppointmentRepository;
import com.example.repository.VideoCallRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@RequiredArgsConstructor
public class VideoCallServiceImpl implements VideoCallService {

    private final VideoCallRepository repository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public VideoCallResponse create(VideoCallRequest req) {
        // Validar que el appointment existe
        Appointment appointment = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cita no encontrada con ID: " + req.getAppointmentId()));

        // Validar que el appointment no tenga ya una videollamada
        if (repository.existsByAppointment_AppointmentId(req.getAppointmentId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe una videollamada para esta cita"
            );
        }

        VideoCall videoCallToSave = VideoCallMapper.toEntity(req);
        videoCallToSave.setAppointment(appointment);

        VideoCall saved = repository.save(videoCallToSave);

        Hibernate.initialize(saved.getAppointment());
        Hibernate.initialize(saved.getAppointment().getPatient());
        Hibernate.initialize(saved.getAppointment().getDoctor());

        return VideoCallMapper.toResponse(saved);
    }

    @Override
    public VideoCallResponse findByAppointmentId(Long appointmentId) {
        // Validar que el appointment existe
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new EntityNotFoundException("Cita no encontrada con ID: " + appointmentId);
        }

        VideoCall videoCall = repository.findByAppointment_AppointmentId(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Videollamada no encontrada para la cita con ID: " + appointmentId));

        Hibernate.initialize(videoCall.getAppointment());
        Hibernate.initialize(videoCall.getAppointment().getPatient());
        Hibernate.initialize(videoCall.getAppointment().getDoctor());

        return VideoCallMapper.toResponse(videoCall);
    }

    @Override
    public VideoCallResponse updateStatus(Long videocallId, String status) {
        // Validar el estado
        if (!status.matches("^(active|ended|cancelled)$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estado debe ser: active, ended o cancelled"
            );
        }

        VideoCall videoCall = repository.findById(videocallId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Videollamada no encontrada con ID: " + videocallId));

        videoCall.setStatus(status);
        VideoCall saved = repository.save(videoCall);

        Hibernate.initialize(saved.getAppointment());
        Hibernate.initialize(saved.getAppointment().getPatient());
        Hibernate.initialize(saved.getAppointment().getDoctor());

        return VideoCallMapper.toResponse(saved);
    }
}