package com.unbosque.mundial_hub.controllers.group;

import com.unbosque.mundial_hub.dto.request.group.SaveGroupRequestDto;
import com.unbosque.mundial_hub.dto.request.group.UpdateGroupRequestDto;
import com.unbosque.mundial_hub.dto.response.auth.CustomUserPrincipal;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.group.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllGroups(
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        var response = groupService.findAll(user.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<?>> getGroupById(@PathVariable UUID groupId) {
        var response = groupService.findById(groupId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createGroup(
            @Valid @RequestBody SaveGroupRequestDto request,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        var response = groupService.create(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }

    @PreAuthorize("""
        @groupService.hasOwnerOrAdminRole(
            #groupId,
            principal.getId()
        )
    """)
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<ApiResponse<?>> generateInviteToken(
            @PathVariable UUID groupId
    ) {
        var response = groupService.generateInviteToken(groupId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }

    @PatchMapping("/invite/{token}")
    public ResponseEntity<ApiResponse<?>> generateInviteToken(
            @PathVariable String token,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        var response = groupService.joinByInviteToken(token, user.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ApiResponse<?>> updateGroup(
            @PathVariable UUID groupId,
            @Valid @RequestBody UpdateGroupRequestDto request,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        var response = groupService.update(groupId, request, user.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }

    @PatchMapping("/{groupId}/leave")
    public ResponseEntity<Void> exitGroup(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        groupService.leaveGroup(groupId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("""
        @groupService.hasOwnerOrAdminRole(
            #groupId,
            principal.getId()
        )
    """)
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {
        groupService.deleteGroup(groupId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
