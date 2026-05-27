package com.unbosque.mundial_hub.repositories.pool;

import com.unbosque.mundial_hub.models.PoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PoolRepository extends JpaRepository<PoolEntity, UUID> {
    boolean existsByCode(String code);
    @Query("""
        SELECT DISTINCT p
        FROM PoolEntity p
        JOIN FETCH p.startsAt
        JOIN UserPoolEntity pm ON pm.pool.id = p.id
        WHERE pm.user.id = :userId
    """)
    List<PoolEntity> findAllByMemberId(UUID userId);

    @Query("""
        SELECT p
        FROM PoolEntity p
        JOIN FETCH p.startsAt
        WHERE p.id = :poolId
    """)
    Optional<PoolEntity> findByIdWithCreator(UUID poolId);
}