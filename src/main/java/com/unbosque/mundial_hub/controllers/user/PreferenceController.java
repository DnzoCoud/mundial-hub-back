package com.unbosque.mundial_hub.controllers.user;

import com.unbosque.mundial_hub.dto.request.user.UpdatePreferencesRequest;
import com.unbosque.mundial_hub.dto.response.auth.CustomUserPrincipal;
import com.unbosque.mundial_hub.dto.response.user.UserPreferencesResponse;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.preference.PreferenceService;
import com.unbosque.mundial_hub.utilities.ParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> getPreferences(
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        UserPreferencesResponse preferences = preferenceService.getPreferences(user.getId());
        return ResponseEntity.ok(ApiResponse.ok(preferences));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> updatePreferences(
            @AuthenticationPrincipal
            CustomUserPrincipal user,
            @Valid @RequestBody UpdatePreferencesRequest request
    ) {
        UserPreferencesResponse updated = preferenceService.updatePreferences(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}