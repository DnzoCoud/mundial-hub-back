package com.unbosque.mundial_hub.controllers;

import com.unbosque.mundial_hub.dto.LoginRequestDTO;
import com.unbosque.mundial_hub.dto.LoginResponseDTO;
import com.unbosque.mundial_hub.dto.RegisterRequestDTO;
import com.unbosque.mundial_hub.dto.RegisterResponseDTO;
import com.unbosque.mundial_hub.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registro (HU-01)
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Datos inválidos");
            return ResponseEntity.badRequest().body(new RegisterResponseDTO(errorMessage, false));
        }
        String result = authService.register(request);
        boolean success = result.equals("Usuario registrado exitosamente");
        return ResponseEntity.ok(new RegisterResponseDTO(result, success));
    }

    // Login (HU-02)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Datos inválidos");
            return ResponseEntity.badRequest().body(new LoginResponseDTO(null, false, errorMsg, null));
        }
        LoginResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}