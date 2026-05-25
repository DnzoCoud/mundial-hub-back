package com.unbosque.mundial_hub.repositories.user;

import com.unbosque.mundial_hub.models.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
}