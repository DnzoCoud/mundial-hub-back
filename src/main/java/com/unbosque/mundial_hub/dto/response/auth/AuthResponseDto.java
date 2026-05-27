package com.unbosque.mundial_hub.dto.response.auth;

import com.unbosque.mundial_hub.dto.domain.user.UserDto;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Long expiresIn,
    UserDto user
) {
}
