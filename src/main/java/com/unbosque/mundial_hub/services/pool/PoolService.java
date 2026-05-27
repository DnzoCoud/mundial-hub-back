package com.unbosque.mundial_hub.services.pool;

import com.unbosque.mundial_hub.dto.domain.pool.PoolResponse;
import com.unbosque.mundial_hub.dto.request.pool.CreatePoolRequest;
import com.unbosque.mundial_hub.dto.response.pool.PoolSummaryResponse;
import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.DomainException;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.pool.PoolMapper;
import com.unbosque.mundial_hub.models.GroupEntity;
import com.unbosque.mundial_hub.models.PoolEntity;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserPoolEntity;
import com.unbosque.mundial_hub.models.enums.PoolStatus;
import com.unbosque.mundial_hub.repositories.group.GroupRepository;
import com.unbosque.mundial_hub.repositories.group.UserGroupRepository;
import com.unbosque.mundial_hub.repositories.pool.PoolRepository;
import com.unbosque.mundial_hub.repositories.pool.UserPoolRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoolService {
    private final PoolRepository poolRepository;
    private final UserPoolRepository poolMemberRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository groupMemberRepository;
    private final PoolMapper poolMapper;
    private final UserRepository userRepository;

    @Transactional
    public PoolResponse create(UUID groupId, CreatePoolRequest request, UUID currentUserId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        System.out.println(currentUserId);

        boolean isMember = groupMemberRepository
                .existsByUser_IdAndGroup_Id(currentUserId, groupId);

        if (!isMember) {
            throw new AlreadyExistsException("You are not member of this group");
        }

        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String code = this.generateUniqueCode();

        PoolEntity pool = new PoolEntity();
        pool.setGroup(group);
        pool.setName(request.name());
        pool.setDescription(request.description());
        pool.setCode(code);
        pool.setMaxMembers(request.maxMembers());
        pool.setPrivate(request.isPrivate());
        pool.setStatus(PoolStatus.ACTIVE);
        pool.setStartsAt(request.startsAt());
        pool.setEndsAt(request.endsAt());

        poolRepository.save(pool);

        UserPoolEntity poolMember = new UserPoolEntity();
        poolMember.setPool(pool);
        poolMember.setUser(user);
        poolMember.setTotalPoints(0);
        poolMember.setJoinedAt(LocalDateTime.now());
        poolMemberRepository.save(poolMember);

        return poolMapper.toPoolResponse(pool);
    }

    @Transactional(readOnly = true)
    public List<PoolSummaryResponse> findAll(UUID userId) {
        List<PoolEntity> pools = poolRepository.findAllByMemberId(userId);
        return pools.stream().map(pool -> {
            UserPoolEntity member = poolMemberRepository
                    .findByPool_IdAndUser_Id(pool.getId(), userId)
                    .orElseThrow();

            int membersCount = poolMemberRepository
                    .countByPool_Id(pool.getId());

            return new PoolSummaryResponse(
                    pool.getId(),
                    pool.getName(),
                    pool.getDescription(),
                    pool.getCode(),
                    pool.getMaxMembers(),
                    pool.isPrivate(),
                    pool.getStatus().name(),
                    member.getTotalPoints(),
                    membersCount
            );
        }).toList();
    }

    @Transactional(readOnly = true)
    public PoolSummaryResponse findById(
            UUID poolId,
            UUID currentUserId
    ) {
        PoolEntity pool = poolRepository.findByIdWithCreator(poolId)
                .orElseThrow(() -> new NotFoundException("Pool not found"));

        UserPoolEntity member = poolMemberRepository
                .findByPool_IdAndUser_Id(poolId, currentUserId)
                .orElseThrow(() ->
                        new DomainException(
                                "You are not member of this pool"
                        )
                );

        int membersCount = poolMemberRepository
                .countByPool_Id(pool.getId());

        return new PoolSummaryResponse(
                pool.getId(),
                pool.getName(),
                pool.getDescription(),
                pool.getCode(),
                pool.getMaxMembers(),
                pool.isPrivate(),
                pool.getStatus().name(),
                member.getTotalPoints(),
                membersCount
        );
    }

    private String generateUniqueCode() {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();

        return random.ints(8, 0, CHARS.length())
                .mapToObj(CHARS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
