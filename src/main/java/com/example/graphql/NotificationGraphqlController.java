package com.example.graphql;

import com.example.dto.NotificationRequest;
import com.example.dto.NotificationResponse;
import com.example.service.NotificationService;
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
public class NotificationGraphqlController {

    private final NotificationService service;

    @QueryMapping
    @PreAuthorize("#userId == authentication.principal.userId") // Solo el propio usuario
    public List<NotificationResponse> getNotificationsByUser(@Argument Long userId, @Argument Integer page, @Argument Integer pageSize) {
        return service.findByUserId(userId, page != null ? page : 0, pageSize != null ? pageSize : 10);
    }

    @QueryMapping
    @PreAuthorize("#userId == authentication.principal.userId")
    public List<NotificationResponse> getUnreadNotifications(@Argument Long userId) {
        return service.findUnreadByUserId(userId);
    }

    @MutationMapping
    @PreAuthorize("hasRole('Admin')") // Normalmente el sistema crea notificaciones, o admin
    public NotificationResponse createNotification(@Valid @Argument NotificationRequest req) {
        return service.create(req);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public NotificationResponse updateNotificationStatus(@Argument Long id, @Argument String status) {
        return service.updateStatus(id, status);
    }
}