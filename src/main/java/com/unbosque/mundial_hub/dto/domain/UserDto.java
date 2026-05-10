package com.unbosque.mundial_hub.dto.domain;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
    UUID id,
    String name,
    String email,
    String status,
    LocalDate createdAt,
    UserProfileDto profile
) {
}
