package com.unbosque.mundial_hub.mappers.user;

import com.unbosque.mundial_hub.dto.domain.user.UserDto;
import com.unbosque.mundial_hub.models.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = { UserProfileMapper.class }
)
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
}
