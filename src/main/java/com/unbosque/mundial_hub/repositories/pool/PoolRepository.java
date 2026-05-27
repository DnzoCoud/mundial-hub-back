package com.unbosque.mundial_hub.repositories.pool;

import com.unbosque.mundial_hub.models.PoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PoolRepository extends JpaRepository<PoolEntity, UUID> {
    boolean existsByCode(String code);
}