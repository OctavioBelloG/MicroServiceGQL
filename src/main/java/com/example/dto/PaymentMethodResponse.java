// src/main/java/com/example/dto/PaymentMethodResponse.java
package com.example.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentMethodResponse {
    Long methodId;
    String methodName;
    String description;
}