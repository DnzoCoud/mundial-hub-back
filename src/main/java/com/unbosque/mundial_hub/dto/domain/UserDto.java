package com.unbosque.mundial_hub.dto.domain;

import com.unbosque.mundial_hub.models.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
    UUID id,
    String name,
    String email,
    String status,
    UserRole role,
    LocalDate createdAt,
    UserProfileDto profile
) {
}
