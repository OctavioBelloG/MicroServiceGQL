package com.example.graphql;

import com.example.dto.*;
import com.example.service.ScheduleService;
import com.example.service.SpecialtyService;
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
public class GenericGraphqlController {

    private final ScheduleService scheduleService;
    private final SpecialtyService specialtyService;

    // --- SCHEDULE ---
    @QueryMapping
    public List<ScheduleResponse> findAllSchedules() {
        return scheduleService.findAll();
    }

    @QueryMapping
    public ScheduleResponse findScheduleById(@Argument Long id) {
        return scheduleService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Doctor')")
    public ScheduleResponse createSchedule(@Argument ScheduleRequest req) {
        return scheduleService.create(req);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Doctor')")
    public ScheduleResponse updateSchedule(@Argument Long id, @Argument ScheduleRequest req) {
        return scheduleService.update(id, req);
    }

    // --- SPECIALTY ---
    @QueryMapping
    public List<SpecialtyResponse> findAllSpecialties() {
        return specialtyService.findAll();
    }

    @QueryMapping
    public SpecialtyResponse findSpecialtyById(@Argument Long id) {
        return specialtyService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Admin')")
    public SpecialtyResponse createSpecialty(@Argument SpecialtyRequest req) {
        return specialtyService.create(req);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Admin')")
    public SpecialtyResponse updateSpecialty(@Argument Long id, @Argument SpecialtyRequest req) {
        return specialtyService.update(id, req);
    }
}