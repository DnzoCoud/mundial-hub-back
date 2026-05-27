package com.unbosque.mundial_hub.services.userProfile;

import com.unbosque.mundial_hub.dto.domain.user.UserProfileDto;
import com.unbosque.mundial_hub.dto.request.user.UpdateProfileRequest;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.user.UserProfileMapper;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import com.unbosque.mundial_hub.repositories.user.UserProfileRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @InjectMocks
    private UserProfileService userProfileService;

    private String email;
    private UserEntity userEntity;
    private UserProfileEntity profileEntity;
    private UpdateProfileRequest updateRequest;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        UUID userId = UUID.randomUUID();
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail(email);
        userEntity.setName("Test User");

        profileEntity = new UserProfileEntity();
        profileEntity.setId(UUID.randomUUID());
        profileEntity.setFullName("Test User");
        profileEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        profileEntity.setCountry("Colombia");
        profileEntity.setCity("Bogotá");
        profileEntity.setAvatarUrl("avatar.png");
        profileEntity.setUser(userEntity);

        userEntity.setProfile(profileEntity);

        updateRequest = new UpdateProfileRequest();
        updateRequest.setFullName("Updated Name");
        updateRequest.setBirthDate(LocalDate.of(2000, 2, 2));
        updateRequest.setCountry("México");
        updateRequest.setCity("CDMX");
        updateRequest.setAvatarUrl("new-avatar.png");
    }

    @Test
    void getProfileByEmail_ShouldReturnUserProfileDto_WhenUserExists() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        UserProfileDto expectedDto = new UserProfileDto(
                profileEntity.getId(),
                profileEntity.getFullName(),
                profileEntity.getBirthDate(),
                profileEntity.getCity(),
                profileEntity.getCountry(),
                profileEntity.getAvatarUrl()
        );
        when(userProfileMapper.toDto(profileEntity)).thenReturn(expectedDto);

        UserProfileDto result = userProfileService.getProfileByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(profileEntity.getFullName());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileMapper, times(1)).toDto(profileEntity);
    }

    @Test
    void getProfileByEmail_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.getProfileByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void getProfileByEmail_ShouldThrowNotFoundException_WhenProfileNull() {
        userEntity.setProfile(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> userProfileService.getProfileByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Perfil no encontrado");
    }

    @Test
    void updateProfile_ShouldUpdateAndReturnDto_WhenValid() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userProfileRepository.save(any(UserProfileEntity.class))).thenReturn(profileEntity);
        // Simular el DTO actualizado devuelto por el mapper
        UserProfileDto updatedDto = new UserProfileDto(
                profileEntity.getId(),
                updateRequest.getFullName(),
                updateRequest.getBirthDate(),
                updateRequest.getCity(),
                updateRequest.getCountry(),
                updateRequest.getAvatarUrl()
        );
        when(userProfileMapper.toDto(any(UserProfileEntity.class))).thenReturn(updatedDto);

        UserProfileDto result = userProfileService.updateProfile(email, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(updateRequest.getFullName());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileRepository, times(1)).save(any(UserProfileEntity.class));
    }

    @Test
    void updateProfile_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.updateProfile(email, updateRequest))
                .isInstanceOf(NotFoundException.class);
    }
}