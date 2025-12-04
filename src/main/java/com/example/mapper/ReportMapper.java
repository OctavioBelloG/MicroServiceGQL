package com.example.mapper;

import com.example.model.Report;
import com.example.model.User;
import com.example.dto.ReportRequest;
import com.example.dto.ReportResponse;

public final class ReportMapper {

    public static ReportResponse toResponse(Report r) {
        if (r == null) return null;
        return ReportResponse.builder()
                .reportId(r.getReportId())
                .reportType(r.getReportType())
                .generatedByUserId(r.getGeneratedBy() != null ? r.getGeneratedBy().getUserId() : null)
                .generatedByUserName(r.getGeneratedBy() != null ? r.getGeneratedBy().getUsername() : null)
                .generatedAt(r.getGeneratedAt())
                .dataJson(r.getData()) // Mapea el String/JSONB
                .build();
    }

    public static Report toEntity(ReportRequest dto) {
        if (dto == null) return null;
        Report r = new Report();
        
        if (dto.getGeneratedByUserId() != null) {
            User user = new User();
            user.setUserId(dto.getGeneratedByUserId());
            r.setGeneratedBy(user);
        }
        
        r.setReportType(dto.getReportType());
        // El campo 'data' y 'generatedAt' se establecen en el Service durante la generación real
        return r;
    }

    public static void copyToEntity(ReportRequest dto, Report entity) {
        if (dto == null || entity == null) return;
        
        if (dto.getGeneratedByUserId() != null) {
            User user = new User();
            user.setUserId(dto.getGeneratedByUserId());
            entity.setGeneratedBy(user);
        }
        
        entity.setReportType(dto.getReportType());
        // NO COPIAR DATA/GENERATED_AT AQUÍ, ya que se regeneran en el servicio
    }
}