package com.unbosque.mundial_hub.dto.domain.pool;

import java.util.UUID;

public record PoolResponse(
        UUID id,
        String name,
        String description,
        String code,
        Integer maxMembers,
        Boolean isPrivate,
        String status
) {
}