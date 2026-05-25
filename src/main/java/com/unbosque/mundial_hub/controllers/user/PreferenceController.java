package com.unbosque.mundial_hub.controllers.user;

import com.unbosque.mundial_hub.dto.request.user.UpdatePreferencesRequest;
import com.unbosque.mundial_hub.dto.response.user.UserPreferencesResponse;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.preference.PreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> getPreferences(Authentication authentication) {
        String email = authentication.getName();
        UserPreferencesResponse preferences = preferenceService.getPreferences(email);
        return ResponseEntity.ok(ApiResponse.ok(preferences));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody UpdatePreferencesRequest request
    ) {
        String email = authentication.getName();
        UserPreferencesResponse updated = preferenceService.updatePreferences(email, request);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}