package com.example.dto;

import lombok.Builder;
import lombok.Value;
@Value
@Builder
public class UserResponse {
  Long userId;
    String userName;
    String email;
    String roleName;
    String status;
}
