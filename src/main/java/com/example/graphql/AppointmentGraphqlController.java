package com.example.graphql;

import com.example.dto.AppointmentRequest;
import com.example.dto.AppointmentResponse;
import com.example.service.AppointmentService;
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
public class AppointmentGraphqlController {

    private final AppointmentService service;

    @QueryMapping
    @PreAuthorize("hasAnyRole('Doctor', 'Admin')")
    public List<AppointmentResponse> findAllAppointments() {
        return service.findAll();
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public AppointmentResponse findAppointmentById(@Argument Long id) {
        return service.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('Paciente', 'Recepcionista')")
    public AppointmentResponse createAppointment(@Valid @Argument AppointmentRequest req) {
        return service.create(req);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public AppointmentResponse updateAppointment(@Argument Long id, @Valid @Argument AppointmentRequest req) {
        return service.update(id, req);
    }
}