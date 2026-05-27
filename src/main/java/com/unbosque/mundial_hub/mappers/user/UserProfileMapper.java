package com.unbosque.mundial_hub.mappers.user;

import com.unbosque.mundial_hub.dto.domain.user.UserProfileDto;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfileEntity entity);
    UserProfileEntity toEntity(UserProfileDto dto);
}
