package com.unbosque.mundial_hub.services.auth;

import com.unbosque.mundial_hub.dto.domain.UserDto;
import com.unbosque.mundial_hub.dto.response.LoginResponseDTO;
import com.unbosque.mundial_hub.dto.request.RegisterRequestDTO;
import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.BadAuthenticationException;
import com.unbosque.mundial_hub.exceptions.DomainException;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import com.unbosque.mundial_hub.mappers.UserMapper;
import com.unbosque.mundial_hub.models.EntityStatus;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import com.unbosque.mundial_hub.repositories.UserProfileRepository;
import com.unbosque.mundial_hub.repositories.UserRepository;
import com.unbosque.mundial_hub.utilities.TokenTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserDto register(RegisterRequestDTO request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new DomainException("Passwords don't match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(EntityStatus.ACTIVE.toString());
        user.setCreatedAt(LocalDate.now());
        user = userRepository.save(user);

        UserProfileEntity profile = new UserProfileEntity();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        userProfileRepository.save(profile);

        return this.userMapper.toDto(user);
    }

    public LoginResponseDTO login(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadAuthenticationException("Bad Credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadAuthenticationException("Bad Credentials");
        }

        String token = jwtService.generateToken(user);
        var userDto = userMapper.toDto(user);

        return new LoginResponseDTO(
            token,
            TokenTypes.BEARER.toString(),
    86400L,
            userDto
        );
    }
}