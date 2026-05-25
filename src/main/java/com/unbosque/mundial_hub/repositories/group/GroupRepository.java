package com.unbosque.mundial_hub.repositories.group;

import com.unbosque.mundial_hub.models.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {
    @Query("""
        SELECT g
        FROM GroupEntity g
        LEFT JOIN FETCH g.users ug
        LEFT JOIN FETCH ug.user
        WHERE g.id = :id
    """)
    Optional<GroupEntity> findByIdWithUsers(UUID id);

    @Query("""
        SELECT g
        FROM GroupEntity g
        LEFT JOIN FETCH g.users ug
        LEFT JOIN FETCH ug.user
        WHERE ug.user.id = :userId
    """)
    List<GroupEntity> findAllWithUsers(UUID userId);

    Optional<GroupEntity> findByInviteToken(String token);
}
