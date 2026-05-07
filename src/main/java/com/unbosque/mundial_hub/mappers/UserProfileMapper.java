package com.unbosque.mundial_hub.mappers;

import com.unbosque.mundial_hub.dto.domain.UserProfileDto;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfileEntity entity);
    UserProfileEntity toEntity(UserProfileDto dto);
}
