package com.unbosque.mundial_hub.services.auth;

import com.unbosque.mundial_hub.dto.domain.user.UserDto;
import com.unbosque.mundial_hub.dto.request.auth.RegisterRequestDTO;
import com.unbosque.mundial_hub.dto.response.auth.LoginResponseDTO;
import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.BadAuthenticationException;
import com.unbosque.mundial_hub.mappers.user.UserMapper;
import com.unbosque.mundial_hub.models.UserEntity;
import com.unbosque.mundial_hub.models.UserProfileEntity;
import com.unbosque.mundial_hub.repositories.user.UserProfileRepository;
import com.unbosque.mundial_hub.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;  // Agregamos mock del mapper

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequest;
    private UserEntity mockUser;
    private UserDto mockUserDto;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO(
                "Juan Pérez",
                "juan@test.com",
                "password123",
                "password123"
        );

        UUID userId = UUID.randomUUID();
        mockUser = new UserEntity();
        mockUser.setId(userId);
        mockUser.setEmail(registerRequest.getEmail());
        mockUser.setName(registerRequest.getFullName());

        mockUserDto = new UserDto(
                userId,
                registerRequest.getFullName(),
                registerRequest.getEmail(),
                "ACTIVE",
                null,
                LocalDate.now(),
                null
        );
    }

    @Test
    void register_ShouldReturnUserDto_WhenValidRequest() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);
        when(userProfileRepository.save(any(UserProfileEntity.class))).thenReturn(new UserProfileEntity());
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(mockUserDto);

        UserDto result = authService.register(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userProfileRepository, times(1)).save(any(UserProfileEntity.class));
        verify(userMapper, times(1)).toDto(any(UserEntity.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any());
        verify(userProfileRepository, never()).save(any());
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        String email = "juan@test.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPass";

        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn("fake-jwt-token");
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        LoginResponseDTO response = authService.login(email, rawPassword);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("fake-jwt-token");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().email()).isEqualTo(email);
        verify(jwtService, times(1)).generateToken(mockUser);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        String email = "notfound@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(email, "any"))
                .isInstanceOf(BadAuthenticationException.class)
                .hasMessageContaining("Bad Credentials");
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        String email = "juan@test.com";
        String rawPassword = "wrong";
        String encodedPassword = "encodedPass";

        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertThatThrownBy(() -> authService.login(email, rawPassword))
                .isInstanceOf(BadAuthenticationException.class)
                .hasMessageContaining("Bad Credentials");
    }
}