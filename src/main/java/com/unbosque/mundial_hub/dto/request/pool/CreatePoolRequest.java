package com.unbosque.mundial_hub.dto.request.pool;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record CreatePoolRequest(
        @NotNull
        @NotBlank
        String name,
        String description,
        @PositiveOrZero
        Integer maxMembers,
        Boolean isPrivate,
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {
}
