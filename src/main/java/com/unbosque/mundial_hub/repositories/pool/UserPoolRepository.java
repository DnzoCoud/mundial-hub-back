package com.unbosque.mundial_hub.repositories.pool;

import com.unbosque.mundial_hub.models.UserPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPoolRepository extends JpaRepository<UserPoolEntity, UUID> {
    Optional<UserPoolEntity> findByPool_IdAndUser_Id(UUID poolId, UUID currentUserId);
    int countByPool_Id(UUID id);
}
