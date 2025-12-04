package com.example.graphql;

import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.service.UserService;
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
public class UserGraphqlController {

    private final UserService service;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse findUserById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    @PreAuthorize("hasRole('Admin')")
    public List<UserResponse> getUsersByRole(@Argument Long roleId, @Argument Integer page, @Argument Integer size) {
        return service.getUsersByRolePaged(roleId, page != null ? page : 0, size != null ? size : 10);
    }

    @MutationMapping
    @PreAuthorize("permitAll()")
    public UserResponse registerUser(@Valid @Argument UserRequest req) {
        return service.create(req); // Usa create en lugar de register si el service es create
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse updateUser(@Argument Long id, @Valid @Argument UserRequest req) {
        return service.update(id, req);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Admin')")
    public UserResponse changeUserStatus(@Argument Long id, @Argument String status) {
        return service.changeStatus(id, status);
    }
}
