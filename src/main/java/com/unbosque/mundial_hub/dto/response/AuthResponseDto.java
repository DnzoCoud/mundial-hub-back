package com.unbosque.mundial_hub.dto.response;

import com.unbosque.mundial_hub.dto.domain.UserDto;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Long expiresIn,
    UserDto user
) {
}
