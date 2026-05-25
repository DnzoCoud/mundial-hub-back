package com.unbosque.mundial_hub.services.userProfile;

import com.unbosque.mundial_hub.dto.domain.user.UserProfileDto;
import com.unbosque.mundial_hub.dto.request.user.UpdateProfileRequest;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.user.UserProfileMapper;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import com.unbosque.mundial_hub.repositories.user.UserProfileRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Transactional(readOnly = true)
    public UserProfileDto getProfileByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserProfileEntity profile = user.getProfile();
        if (profile == null) {
            throw new NotFoundException("Perfil no encontrado para el usuario");
        }
        return userProfileMapper.toDto(profile);
    }

    @Transactional
    public UserProfileDto updateProfile(String email, UpdateProfileRequest request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserProfileEntity profile = user.getProfile();
        if (profile == null) {
            // Si por alguna razón no existe el perfil, lo creamos
            profile = new UserProfileEntity();
            profile.setUser(user);
        }

        // Actualizar campos permitidos
        profile.setFullName(request.getFullName());
        profile.setBirthDate(request.getBirthDate());
        profile.setCountry(request.getCountry());
        profile.setAvatarUrl(request.getAvatarUrl());

        userProfileRepository.save(profile);

        // Sincronizar el nombre en UserEntity (opcional pero recomendado)
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setName(request.getFullName());
            userRepository.save(user);
        }

        return userProfileMapper.toDto(profile);
    }
}