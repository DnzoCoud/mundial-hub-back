package com.unbosque.mundial_hub.services.pool;

import com.unbosque.mundial_hub.dto.domain.pool.PoolResponse;
import com.unbosque.mundial_hub.dto.request.pool.CreatePoolRequest;
import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.pool.PoolMapper;
import com.unbosque.mundial_hub.models.GroupEntity;
import com.unbosque.mundial_hub.models.PoolEntity;
import com.unbosque.mundial_hub.models.enums.PoolStatus;
import com.unbosque.mundial_hub.repositories.group.GroupRepository;
import com.unbosque.mundial_hub.repositories.group.UserGroupRepository;
import com.unbosque.mundial_hub.repositories.pool.PoolRepository;
import com.unbosque.mundial_hub.repositories.pool.UserPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
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

    @Transactional
    public PoolResponse create(UUID groupId, CreatePoolRequest request, UUID currentUserId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        boolean isMember = groupMemberRepository
                .existsByUserIdAndGroupId(groupId, currentUserId);

        if (!isMember) {
            throw new AlreadyExistsException("You are not member of this group");
        }

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

        return poolMapper.toPoolResponse(pool);
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
