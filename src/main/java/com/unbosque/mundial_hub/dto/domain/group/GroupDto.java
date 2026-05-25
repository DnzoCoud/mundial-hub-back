package com.unbosque.mundial_hub.dto.domain.group;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GroupDto(
        UUID id,
        String name,
        List<GroupUserDto> users
) {
}
