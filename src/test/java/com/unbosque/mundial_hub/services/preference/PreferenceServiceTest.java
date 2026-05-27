package com.unbosque.mundial_hub.services.preference;

import com.unbosque.mundial_hub.dto.request.user.UpdatePreferencesRequest;
import com.unbosque.mundial_hub.dto.response.user.UserPreferencesResponse;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.models.PreferenceEntity;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.repositories.personal_agenda.PreferenceRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreferenceServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreferenceRepository preferenceRepository;

    @InjectMocks
    private PreferenceService preferenceService;

    private UUID userId;
    private UserEntity userEntity;
    private UpdatePreferencesRequest updateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userEntity = new UserEntity();
        userEntity.setId(userId);

        updateRequest = new UpdatePreferencesRequest();
        updateRequest.setFavoriteTeams(List.of("Colombia", "Brasil"));
        updateRequest.setFavoriteCities(List.of("Bogotá", "Medellín"));
        updateRequest.setFavoriteStadiums(List.of("El Campín", "Atanasio"));
    }

    @Test
    void getPreferences_ShouldReturnUserPreferencesResponse_WhenUserExists() {
        // Simular preferencias existentes
        PreferenceEntity pref1 = new PreferenceEntity();
        pref1.setCategory("FAVORITE_TEAM");
        pref1.setValue("Colombia");
        PreferenceEntity pref2 = new PreferenceEntity();
        pref2.setCategory("FAVORITE_TEAM");
        pref2.setValue("Brasil");
        PreferenceEntity pref3 = new PreferenceEntity();
        pref3.setCategory("FAVORITE_CITY");
        pref3.setValue("Bogotá");

        List<PreferenceEntity> preferences = List.of(pref1, pref2, pref3);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(preferenceRepository.findByUser(userEntity)).thenReturn(preferences);

        UserPreferencesResponse response = preferenceService.getPreferences(userId);

        assertThat(response).isNotNull();
        assertThat(response.getFavoriteTeams()).containsExactly("Colombia", "Brasil");
        assertThat(response.getFavoriteCities()).containsExactly("Bogotá");
        assertThat(response.getFavoriteStadiums()).isEmpty();
    }

    @Test
    void getPreferences_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> preferenceService.getPreferences(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void updatePreferences_ShouldDeleteExistingAndSaveNew_WhenValidRequest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        // No simulamos comportamiento de deleteByUser porque es void
        // Simulamos que después de guardar, findByUser devuelve lista vacía (aunque no es crítico)
        when(preferenceRepository.findByUser(userEntity)).thenReturn(new ArrayList<>());

        UserPreferencesResponse response = preferenceService.updatePreferences(userId, updateRequest);

        assertThat(response).isNotNull();
        // Verificamos que se haya invocado deleteByUser exactamente una vez
        verify(preferenceRepository, times(1)).deleteByUser(userEntity);
        // Verificamos que se haya llamado a save al menos una vez (sin importar cuántas)
        verify(preferenceRepository, atLeastOnce()).save(any(PreferenceEntity.class));
    }

    @Test
    void updatePreferences_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> preferenceService.updatePreferences(userId, updateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}