package com.unbosque.mundial_hub.dto.domain.group;

import java.time.LocalDateTime;
import java.util.UUID;

public record GroupUserDto(
        UUID id,
        String name,
        String email,
        LocalDateTime joinedAt,
        String role
) {
}
