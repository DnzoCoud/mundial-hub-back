package com.unbosque.mundial_hub.services;

import com.unbosque.mundial_hub.dto.LoginRequestDTO;
import com.unbosque.mundial_hub.dto.LoginResponseDTO;
import com.unbosque.mundial_hub.dto.RegisterRequestDTO;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import com.unbosque.mundial_hub.repositories.UserProfileRepository;
import com.unbosque.mundial_hub.repositories.UserRepository;
import com.unbosque.mundial_hub.utilities.SimplePasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // Registro (HU-01)
    @Transactional
    public String register(RegisterRequestDTO request) {
        // Validar contraseñas coincidentes
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Las contraseñas no coinciden";
        }
        // Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            return "El correo ya está registrado";
        }

        // Crear entidad User
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getFullName());          // nombre completo también en User.name
        user.setPassword(SimplePasswordEncoder.encode(request.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        // Crear perfil asociado
        UserProfileEntity profile = new UserProfileEntity();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        // Los demás campos (birthDate, city, country, avatarUrl) se dejan nulos por ahora
        userProfileRepository.save(profile);

        // (Opcional) La agenda personal se creará en HU-07

        return "Usuario registrado exitosamente";
    }

    // Login (HU-02) - versión simplificada sin JWT
    public LoginResponseDTO login(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new LoginResponseDTO(null, false,
                    "No encontramos una cuenta con ese correo. ¿Deseas registrarte?", null);
        }
        if (!SimplePasswordEncoder.matches(rawPassword, user.getPassword())) {
            return new LoginResponseDTO(null, false, "Correo o contraseña incorrectos", null);
        }

        // Generar token simulado (reemplazar después con JWT real)
        String fakeToken = "fake-jwt-token-" + UUID.randomUUID().toString();
        String fullName = (user.getProfile() != null && user.getProfile().getFullName() != null)
                ? user.getProfile().getFullName()
                : user.getName();

        return new LoginResponseDTO(fakeToken, true, "Inicio de sesión exitoso", fullName);
    }
}