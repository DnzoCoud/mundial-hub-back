package com.unbosque.mundial_hub.dto.domain;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileDto(
    UUID id,
    String fullName,
    LocalDate birthDate,
    String city,
    String country,
    String avatarUrl
) {
}
