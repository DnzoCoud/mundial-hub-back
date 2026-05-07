package com.unbosque.mundial_hub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String fullName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo electrónico válido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña")
    private String confirmPassword;
}