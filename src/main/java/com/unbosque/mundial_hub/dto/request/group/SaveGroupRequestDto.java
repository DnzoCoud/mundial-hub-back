package com.unbosque.mundial_hub.dto.request.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SaveGroupRequestDto(
        @NotNull
        @NotBlank
        @Size(min = 3, max = 80)
        String name,
        List<GroupUserRequest> users
) {
}
