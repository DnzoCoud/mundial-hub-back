package com.unbosque.mundial_hub.mappers.group;

import com.unbosque.mundial_hub.dto.domain.group.GroupDto;
import com.unbosque.mundial_hub.dto.domain.group.GroupUserDto;
import com.unbosque.mundial_hub.models.GroupEntity;
import com.unbosque.mundial_hub.models.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(target = "users", source = "users")
    GroupDto toDto(GroupEntity group);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "joinedAt", source = "joinedAt")
    @Mapping(target = "role", expression = "java(userGroup.getRole().name())")
    GroupUserDto toGroupUserDto(UserGroup userGroup);
}
