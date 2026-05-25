package com.unbosque.mundial_hub.repositories.group;

import com.unbosque.mundial_hub.models.GroupEntity;
import com.unbosque.mundial_hub.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    @Query("""
        SELECT COUNT(ug) > 0
        FROM UserGroup ug
        WHERE ug.user.id = :userId
          AND ug.group.id = :groupId
          AND ug.role IN ('ADMIN', 'OWNER')
    """)
    boolean hasAdminAccess(
            UUID userId,
            UUID groupId
    );

    boolean existsByUserIdAndGroupId(
            UUID userId,
            UUID groupId
    );
}
