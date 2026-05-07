package com.unbosque.mundial_hub.controllers;

import com.unbosque.mundial_hub.dto.request.LoginRequestDTO;
import com.unbosque.mundial_hub.dto.response.LoginResponseDTO;
import com.unbosque.mundial_hub.dto.request.RegisterRequestDTO;
import com.unbosque.mundial_hub.dto.response.RegisterResponseDTO;
import com.unbosque.mundial_hub.handlers.ApiResponse;
import com.unbosque.mundial_hub.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequestDTO request) {
        var result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(result));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}