package com.example.graphql;

import com.example.dto.MedicalInsuranceRequest;
import com.example.dto.MedicalInsuranceResponse;
import com.example.dto.MedicalRecordRequest;
import com.example.dto.MedicalRecordResponse;
import com.example.service.MedicalInsuranceService;
import com.example.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalGraphqlController {

    private final MedicalRecordService recordService;
    private final MedicalInsuranceService insuranceService;

    // --- RECORD ---
    @QueryMapping
    @PreAuthorize("hasAnyRole('Doctor', 'Paciente')")
    public MedicalRecordResponse getMedicalRecordByPatient(@Argument Long patientId) {
        return recordService.findByPatientId(patientId);
    }

    @QueryMapping
    @PreAuthorize("hasRole('Doctor')")
    public List<MedicalRecordResponse> searchMedicalRecords(@Argument String keyword) {
        return recordService.searchRecords(keyword);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Doctor')")
    public MedicalRecordResponse createMedicalRecord(@Valid @Argument MedicalRecordRequest req) {
        return recordService.create(req);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Doctor')")
    public MedicalRecordResponse updateMedicalRecord(@Argument Long id, @Valid @Argument MedicalRecordRequest req) {
        return recordService.update(id, req);
    }
    
    @MutationMapping
    @PreAuthorize("hasRole('Doctor')")
    public MedicalRecordResponse updateMedicalRecordDate(@Argument Long id) {
        return recordService.updateLastUpdatedDate(id);
    }

    // --- INSURANCE ---
    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<MedicalInsuranceResponse> getInsuranceByPatient(@Argument Long patientId) {
        return insuranceService.getPoliciesByPatientId(patientId);
    }

    @QueryMapping
    @PreAuthorize("hasRole('Admin')")
    public List<MedicalInsuranceResponse> getExpiredPolicies() {
        return insuranceService.getExpiredPolicies();
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public MedicalInsuranceResponse findInsuranceById(@Argument Long id) {
        return insuranceService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('Paciente', 'Admin')")
    public MedicalInsuranceResponse createInsurance(@Valid @Argument MedicalInsuranceRequest req) {
        return insuranceService.create(req);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('Paciente', 'Admin')")
    public MedicalInsuranceResponse updateInsurance(@Argument Long id, @Valid @Argument MedicalInsuranceRequest req) {
        return insuranceService.update(id, req);
    }
}