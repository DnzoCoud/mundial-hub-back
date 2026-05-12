package com.unbosque.mundial_hub.repositories;

import com.unbosque.mundial_hub.models.PreferenceEntity;
import com.unbosque.mundial_hub.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PreferenceRepository extends JpaRepository<PreferenceEntity, UUID> {
    List<PreferenceEntity> findByUser(UserEntity user);
    void deleteByUser(UserEntity user);
}