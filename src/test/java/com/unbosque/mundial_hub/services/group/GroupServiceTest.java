package com.unbosque.mundial_hub.services.group;

import com.unbosque.mundial_hub.dto.domain.group.GroupDto;
import com.unbosque.mundial_hub.dto.domain.group.GroupUserDto;
import com.unbosque.mundial_hub.dto.request.group.GroupUserRequest;
import com.unbosque.mundial_hub.dto.request.group.SaveGroupRequestDto;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.group.GroupMapper;
import com.unbosque.mundial_hub.models.GroupEntity;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserGroup;
import com.unbosque.mundial_hub.models.enums.GroupRole;
import com.unbosque.mundial_hub.repositories.group.GroupRepository;
import com.unbosque.mundial_hub.repositories.group.UserGroupRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import com.unbosque.mundial_hub.services.common.InviteTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private InviteTokenService inviteTokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    private UUID ownerId;
    private UserEntity ownerUser;
    private SaveGroupRequestDto createRequest;
    private GroupEntity groupEntity;
    private GroupDto groupDto;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        ownerUser = new UserEntity();
        ownerUser.setId(ownerId);
        ownerUser.setName("Owner");
        ownerUser.setEmail("owner@test.com");

        createRequest = new SaveGroupRequestDto("Mi Grupo", new ArrayList<>());

        UUID groupId = UUID.randomUUID();
        groupEntity = new GroupEntity();
        groupEntity.setId(groupId);
        groupEntity.setName("Mi Grupo");

        groupDto = GroupDto.builder()
                .id(groupId)
                .name("Mi Grupo")
                .users(new ArrayList<>())
                .build();
    }

    @Test
    void create_ShouldSaveGroupAndOwnerRelation_WhenValidRequest() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(groupRepository.save(any(GroupEntity.class))).thenReturn(groupEntity);
        when(userGroupRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(groupMapper.toDto(groupEntity)).thenReturn(groupDto);

        GroupDto result = groupService.create(createRequest, ownerId);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Mi Grupo");
        verify(groupRepository, times(1)).save(any(GroupEntity.class));
        verify(userGroupRepository, times(1)).saveAll(anyList());
        verify(groupMapper, times(1)).toDto(groupEntity);
    }

    @Test
    void create_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.create(createRequest, ownerId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");

        verify(groupRepository, never()).save(any());
        verify(userGroupRepository, never()).saveAll(anyList());
    }

    @Test
    void findAll_ShouldReturnListOfGroupDtos_WhenUserHasGroups() {
        List<GroupEntity> groups = List.of(groupEntity);
        when(groupRepository.findAllWithUsers(ownerId)).thenReturn(groups);
        when(groupMapper.toDto(groupEntity)).thenReturn(groupDto);

        List<GroupDto> result = groupService.findAll(ownerId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Mi Grupo");
        verify(groupRepository, times(1)).findAllWithUsers(ownerId);
        verify(groupMapper, times(1)).toDto(groupEntity);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenUserHasNoGroups() {
        when(groupRepository.findAllWithUsers(ownerId)).thenReturn(new ArrayList<>());

        List<GroupDto> result = groupService.findAll(ownerId);

        assertThat(result).isEmpty();
        verify(groupRepository, times(1)).findAllWithUsers(ownerId);
        verify(groupMapper, never()).toDto(any());
    }
}