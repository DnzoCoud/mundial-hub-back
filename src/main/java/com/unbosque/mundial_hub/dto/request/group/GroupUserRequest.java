package com.unbosque.mundial_hub.dto.request.group;

import com.unbosque.mundial_hub.models.enums.GroupRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GroupUserRequest(
        @NotNull
        UUID userId,
        @NotNull
        GroupRole role
) {
}
