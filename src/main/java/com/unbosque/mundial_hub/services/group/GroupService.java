package com.unbosque.mundial_hub.services.group;

import com.unbosque.mundial_hub.dto.domain.group.GroupDto;
import com.unbosque.mundial_hub.dto.request.group.GeneratedInviteLink;
import com.unbosque.mundial_hub.dto.request.group.GroupUserRequest;
import com.unbosque.mundial_hub.dto.request.group.SaveGroupRequestDto;
import com.unbosque.mundial_hub.dto.request.group.UpdateGroupRequestDto;
import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.DomainException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserGroupRepository userGroupRepository;
    private final InviteTokenService inviteTokenService;
    private final UserRepository userRepository;

    public List<GroupDto> findAll(UUID ownerId) {
        return groupRepository.findAllWithUsers(ownerId)
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    public GroupDto findById(UUID id) {
        GroupEntity group = groupRepository.findByIdWithUsers(id)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto create(SaveGroupRequestDto request, UUID ownerId) {
        UserEntity authUser = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        GroupEntity group = new GroupEntity();
        group.setName(request.name());
        group = groupRepository.save(group);

        List<UserGroup> relations = new ArrayList<>();
        UserGroup ownerRelation = new UserGroup();

        ownerRelation.setGroup(group);
        ownerRelation.setUser(authUser);
        ownerRelation.setRole(GroupRole.OWNER);
        ownerRelation.setJoinedAt(LocalDateTime.now());

        relations.add(ownerRelation);

        relations.addAll(
                buildUserRelations(
                        group,
                        request.users(),
                        ownerId
                )
        );

        userGroupRepository.saveAll(relations);
        group.setUsers(relations);

        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto update(UUID groupId, UpdateGroupRequestDto request, UUID ownerId) {
        GroupEntity group = groupRepository.findByIdWithUsers(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        group.setName(request.name());

        if(request.users() != null) {
            List<UserGroup> removableRelations = group.getUsers()
                    .stream()
                    .filter(relation ->
                            relation.getRole() != GroupRole.OWNER
                    )
                    .toList();

            userGroupRepository.deleteAll(removableRelations);

            group.getUsers().removeAll(removableRelations);

            List<UserGroup> newRelations = buildUserRelations(
                    group,
                    request.users(),
                    ownerId
            );

            userGroupRepository.saveAll(newRelations);

            group.getUsers().addAll(newRelations);
        }

        return groupMapper.toDto(group);
    }

    private List<UserGroup> buildUserRelations(
            GroupEntity group,
            List<GroupUserRequest> requestUsers,
            UUID authenticatedUserId
    ) {

        if (requestUsers == null || requestUsers.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> userIds = requestUsers.stream()
                .map(GroupUserRequest::userId)
                .toList();

        List<UserEntity> users = userRepository.findAllById(userIds);

        Map<UUID, UserEntity> usersMap = users.stream()
                .collect(Collectors.toMap(
                        UserEntity::getId,
                        Function.identity()
                ));

        List<UserGroup> relations = new ArrayList<>();

        for (GroupUserRequest requestUser : requestUsers) {

            validateAssignableRole(requestUser.role());

            if (requestUser.userId().equals(authenticatedUserId)) {
                continue;
            }

            UserEntity user = usersMap.get(requestUser.userId());

            if (user == null) {
                continue;
            }

            UserGroup relation = new UserGroup();

            relation.setGroup(group);
            relation.setUser(user);
            relation.setRole(requestUser.role());
            relation.setJoinedAt(LocalDateTime.now());

            relations.add(relation);
        }

        return relations;
    }

    @Transactional
    public GeneratedInviteLink generateInviteToken(UUID groupId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        if (group.getInviteToken() != null) {
            return GeneratedInviteLink.builder()
                    .inviteLink(group.getInviteToken())
                    .build();
        }

        String token = inviteTokenService.generateInviteToken(groupId);

        group.setInviteToken(token);
        groupRepository.save(group);

        return GeneratedInviteLink.builder()
                .inviteLink(token)
                .build();
    }

    @Transactional(readOnly = true)
    public boolean hasOwnerOrAdminRole(UUID groupId, UUID ownerId) {
        return userGroupRepository.hasAdminAccess(groupId, ownerId);
    }

    @Transactional
    public GroupDto joinByInviteToken(
            String token,
            UUID userId
    ) {

        GroupEntity group = groupRepository
                .findByInviteToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid invite token"));

        boolean alreadyExists = userGroupRepository
                .existsByUserIdAndGroupId(
                        userId,
                        group.getId()
                );

        if (alreadyExists) throw new AlreadyExistsException("User already belongs to this group");

        UserEntity user = userRepository.findById(userId)
                .orElseThrow();

        UserGroup relation = new UserGroup();

        relation.setGroup(group);
        relation.setUser(user);
        relation.setRole(GroupRole.MEMBER);
        relation.setJoinedAt(LocalDateTime.now());

        userGroupRepository.save(relation);

        group.getUsers().add(relation);

        return groupMapper.toDto(group);
    }

    private void validateAssignableRole(GroupRole role) {
        if (role == GroupRole.OWNER) {
            throw new DomainException(
                    "OWNER role cannot be assigned"
            );
        }
    }
}
