package com.unbosque.mundial_hub.controllers.user;

import com.unbosque.mundial_hub.dto.domain.user.UserProfileDto;
import com.unbosque.mundial_hub.dto.request.user.UpdateProfileRequest;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.userProfile.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        UserProfileDto profile = userProfileService.getProfileByEmail(userEmail);
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        String userEmail = authentication.getName();
        UserProfileDto updated = userProfileService.updateProfile(userEmail, request);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}