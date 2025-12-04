package com.example.service;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.model.Doctor;
import com.example.dto.DoctorRequest;
import com.example.dto.DoctorResponse;
import com.example.dto.PatientResponse;
import com.example.mapper.DoctorMapper;
import com.example.repository.AppointmentRepository;
import com.example.repository.DoctorRepository;
import com.example.repository.PatientRepository;
import com.example.repository.SpecialtyRepository;
import com.example.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;
    private final UserRepository userRepository; // REQUERIDO: Para validar si fk_user_id existe.
    private final SpecialtyRepository specialtyRepository; // REQUERIDO: Para validar si fk_specialty_id existe.
    private final AppointmentRepository appointmentRepository; // REQUERIDO: Para getDoctorPatients.
    private final PatientRepository patientRepository;
    
    @Override
    public DoctorResponse findById(Long doctorId) {
        Doctor doctor = repository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + doctorId));
        return DoctorMapper.toResponse(doctor);
    }
    
  @Override
public DoctorResponse create(DoctorRequest req) {
    // 1. Validar unicidad del registration number
    if (repository.existsByRegistrationNumber(req.getRegistrationNumber())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de registro ya existe.");
    }
    
    // 2. Validar existencia de fk_user_id
    if (!userRepository.existsById(req.getUserId())) {
        throw new EntityNotFoundException("User not found with ID: " + req.getUserId());
    }

    // 3. Validar existencia de fk_specialty_id
    if (!specialtyRepository.existsById(req.getSpecialtyId())) {
        throw new EntityNotFoundException("Specialty not found with ID: " + req.getSpecialtyId());
    }

    // Nota: También podrías validar aquí que el userId no esté ya asignado a otro Doctor.
    
    Doctor saved = repository.save(DoctorMapper.toEntity(req));
    return DoctorMapper.toResponse(saved);
}
    @Override
    public DoctorResponse update(Long doctorId, DoctorRequest req) {
        Doctor existing = repository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + doctorId));

        // Validar unicidad del registration number solo si cambia
        if (!existing.getRegistrationNumber().equals(req.getRegistrationNumber()) && 
            repository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de registro ya está en uso.");
        }
        
        DoctorMapper.copyToEntity(req, existing);
        Doctor saved = repository.save(existing);
        return DoctorMapper.toResponse(saved);
    }

    // -- MÉTODOS DE CONSULTA (RUTAS) --

    @Override
    public List<DoctorResponse> getDoctorsPaged(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Doctor> doctors = repository.findAll(pageReq);
        return doctors.getContent().stream().map(DoctorMapper::toResponse).toList();
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialty(Long specialtyId, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Doctor> doctors = repository.findBySpecialty_SpecialtyId(specialtyId, pageReq);
        return doctors.getContent().stream().map(DoctorMapper::toResponse).toList();
    }

    @Override
    public List<DoctorResponse> getTopRatedDoctors(int limit) {
        PageRequest pageReq = PageRequest.of(0, limit);
        return repository.findTopByOrderByRatingDesc(pageReq).stream()
                .map(DoctorMapper::toResponse)
                .toList();
    }
    
    // RUTA: GET /doctors/available?date=&specialty=
    @Override
    public List<DoctorResponse> getAvailableDoctors(String date, Long specialtyId) {
        // Implementación simulada: Se requiere lógica avanzada que combine Schedules y Appointments.
        // Por ahora, devolvemos una lista vacía o implementamos una lógica simple de ejemplo.
        System.out.println("Buscando disponibilidad para fecha: " + date + " y especialidad ID: " + specialtyId);
        return List.of(); 
    }
    
    // RUTA: GET /doctors/{id}/patients?page=&size=
    @Override
public List<PatientResponse> getDoctorPatients(Long doctorId, int page, int pageSize) {
    // 1. Verificar si el doctor existe
    if (!repository.existsById(doctorId)) {
         throw new EntityNotFoundException("Doctor not found with ID: " + doctorId);
    }
    
    PageRequest pageReq = PageRequest.of(page, pageSize);
    
    // 2. Lógica para obtener los pacientes a través de las citas
    // Esta lógica requiere que agregues un método a AppointmentRepository:
    /* public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
        @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.doctorId = :doctorId")
        Page<Patient> findDistinctPatientsByDoctorId(Long doctorId, Pageable pageable);
    }
    */
    
    // 3. Uso del método (requiere el método en AppointmentRepository y PatientMapper)
    // Page<Patient> patients = appointmentRepository.findDistinctPatientsByDoctorId(doctorId, pageReq);
    
    // return patients.getContent().stream().map(PatientMapper::toResponse).toList();
    
    // Por ahora, devolvemos un error o una lista vacía hasta tener el AppointmentRepository:
    throw new UnsupportedOperationException("El método getDoctorPatients requiere la implementación de findDistinctPatientsByDoctorId en AppointmentRepository.");
}
}
