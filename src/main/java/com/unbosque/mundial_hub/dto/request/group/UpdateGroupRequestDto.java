package com.unbosque.mundial_hub.dto.request.group;

import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateGroupRequestDto (
    String name,
    List<GroupUserRequest> users
) { }