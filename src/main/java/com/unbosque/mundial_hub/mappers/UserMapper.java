package com.unbosque.mundial_hub.mappers;

import com.unbosque.mundial_hub.dto.domain.UserDto;
import com.unbosque.mundial_hub.models.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = { UserProfileMapper.class }
)
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto dto);
}
