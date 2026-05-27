package com.unbosque.mundial_hub.dto.response.pool;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PoolSummaryResponse(
        UUID id,
        String name,
        String description,
        String code,
        Integer maxMembers,
        Boolean isPrivate,
        String status,
        Integer totalPoints,
        Integer membersCount
) {
}
