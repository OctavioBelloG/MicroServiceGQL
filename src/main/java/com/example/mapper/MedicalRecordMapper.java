package com.example.mapper;

import com.example.model.MedicalRecord;
import com.example.model.Patient;
import com.example.dto.MedicalRecordRequest;
import com.example.dto.MedicalRecordResponse;

public final class MedicalRecordMapper {

    public static MedicalRecordResponse toResponse(MedicalRecord m) {
        if (m == null) return null;
        return MedicalRecordResponse.builder()
                .recordId(m.getRecordId())
                .patientId(m.getPatient() != null ? m.getPatient().getPatientId() : null)
                .allergies(m.getAllergies())
                .vaccines(m.getVaccines())
                .medicalHistory(m.getMedicalHistory())
                .notes(m.getNotes())
                .lastUpdated(m.getLastUpdated())
                .build();
    }

    public static MedicalRecord toEntity(MedicalRecordRequest dto) {
        if (dto == null) return null;
        MedicalRecord m = new MedicalRecord();
        m.setAllergies(dto.getAllergies());
        m.setVaccines(dto.getVaccines());
        m.setMedicalHistory(dto.getMedicalHistory());
        m.setNotes(dto.getNotes());
        m.setLastUpdated(dto.getLastUpdated());
        if (dto.getPatientId() != null) {
            Patient p = new Patient();
            p.setPatientId(dto.getPatientId());
            m.setPatient(p);
        }
        return m;
    }

    public static void copyToEntity(MedicalRecordRequest dto, MedicalRecord entity) {
        if (dto == null || entity == null) return;
        entity.setAllergies(dto.getAllergies());
        entity.setVaccines(dto.getVaccines());
        entity.setMedicalHistory(dto.getMedicalHistory());
        entity.setNotes(dto.getNotes());
        entity.setLastUpdated(dto.getLastUpdated());
        if (dto.getPatientId() != null) {
            Patient p = new Patient();
            p.setPatientId(dto.getPatientId());
            entity.setPatient(p);
        } else {
            entity.setPatient(null);
        }
    }
}
