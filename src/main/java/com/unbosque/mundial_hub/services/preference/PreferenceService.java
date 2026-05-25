package com.unbosque.mundial_hub.services.preference;

import com.unbosque.mundial_hub.dto.request.user.UpdatePreferencesRequest;
import com.unbosque.mundial_hub.dto.response.user.UserPreferencesResponse;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.models.PreferenceEntity;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.repositories.personal_agenda.PreferenceRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;

    @Transactional(readOnly = true)
    public UserPreferencesResponse getPreferences(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        List<PreferenceEntity> preferences = preferenceRepository.findByUser(user);

        List<String> favoriteTeams = new ArrayList<>();
        List<String> favoriteCities = new ArrayList<>();
        List<String> favoriteStadiums = new ArrayList<>();

        for (PreferenceEntity pref : preferences) {
            switch (pref.getCategory()) {
                case "FAVORITE_TEAM":
                    favoriteTeams.add(pref.getValue());
                    break;
                case "FAVORITE_CITY":
                    favoriteCities.add(pref.getValue());
                    break;
                case "FAVORITE_STADIUM":
                    favoriteStadiums.add(pref.getValue());
                    break;
            }
        }

        return new UserPreferencesResponse(favoriteTeams, favoriteCities, favoriteStadiums);
    }

    @Transactional
    public UserPreferencesResponse updatePreferences(UUID id, UpdatePreferencesRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Eliminar preferencias existentes del usuario
        preferenceRepository.deleteByUser(user);

        // Guardar nuevas preferencias
        savePreferences(user, request.getFavoriteTeams(), "FAVORITE_TEAM");
        savePreferences(user, request.getFavoriteCities(), "FAVORITE_CITY");
        savePreferences(user, request.getFavoriteStadiums(), "FAVORITE_STADIUM");

        // Retornar las preferencias guardadas
        return getPreferences(user.getId());
    }

    private void savePreferences(UserEntity user, List<String> values, String category) {
        if (values == null || values.isEmpty()) {
            return;
        }
        // Filtrar valores vacíos o nulos
        List<String> validValues = values.stream()
                .filter(v -> v != null && !v.trim().isEmpty())
                .toList();

        for (String value : validValues) {
            PreferenceEntity pref = new PreferenceEntity();
            pref.setUser(user);
            pref.setCategory(category);
            pref.setValue(value.trim());
            preferenceRepository.save(pref);
        }
    }
}