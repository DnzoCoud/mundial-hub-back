package com.unbosque.mundial_hub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        @Pattern(regexp = "^[a-zA-ZáéíóúñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
        private String fullName;

        @Past(message = "La fecha de nacimiento debe ser pasada")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate birthDate;

        private String country;

        private String avatarUrl;
    }

