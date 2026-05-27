package com.unbosque.mundial_hub.controllers.pool;

import com.unbosque.mundial_hub.dto.domain.pool.PoolResponse;
import com.unbosque.mundial_hub.dto.request.pool.CreatePoolRequest;
import com.unbosque.mundial_hub.dto.response.auth.CustomUserPrincipal;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.pool.PoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pools")
@RequiredArgsConstructor
public class PoolController {
    private final PoolService poolService;

    @PostMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<?>> createPool(
            @PathVariable UUID groupId,
            @RequestBody @Valid CreatePoolRequest request,
            @AuthenticationPrincipal
            CustomUserPrincipal user
    ) {

        PoolResponse response = poolService.create(
                groupId,
                request,
                user.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }
}
