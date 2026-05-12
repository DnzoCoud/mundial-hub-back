package com.unbosque.mundial_hub.controllers;

import com.unbosque.mundial_hub.dto.domain.UserProfileDto;
import com.unbosque.mundial_hub.dto.request.UpdateProfileRequest;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.auth.userProfile.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDto profile = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        String email = authentication.getName();
        UserProfileDto updated = userProfileService.updateProfile(email, request);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}