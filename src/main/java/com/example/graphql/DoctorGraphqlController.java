package com.example.graphql;

import com.example.dto.DoctorRequest;
import com.example.dto.DoctorResponse;
import com.example.service.DoctorService;
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
public class DoctorGraphqlController {

    private final DoctorService service;

    @QueryMapping
    @PreAuthorize("permitAll()") // O "isAuthenticated()"
    public List<DoctorResponse> getDoctorsPaged(@Argument Integer page, @Argument Integer size) {
        return service.getDoctorsPaged(page != null ? page : 0, size != null ? size : 10);
    }

    @QueryMapping
    @PreAuthorize("permitAll()")
    public DoctorResponse findDoctorById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    @PreAuthorize("permitAll()")
    public List<DoctorResponse> getDoctorsBySpecialty(@Argument Long specialtyId, @Argument Integer page, @Argument Integer size) {
        return service.getDoctorsBySpecialty(specialtyId, page != null ? page : 0, size != null ? size : 10);
    }

    @QueryMapping
    @PreAuthorize("permitAll()")
    public List<DoctorResponse> getTopRatedDoctors(@Argument Integer limit) {
        return service.getTopRatedDoctors(limit != null ? limit : 10);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Admin')") 
    public DoctorResponse createDoctor(@Valid @Argument DoctorRequest req) {
        return service.create(req);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('Admin', 'Doctor')")
    public DoctorResponse updateDoctor(@Argument Long id, @Valid @Argument DoctorRequest req) {
        return service.update(id, req);
    }
}