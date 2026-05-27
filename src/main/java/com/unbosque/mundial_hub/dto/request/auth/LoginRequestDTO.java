package com.unbosque.mundial_hub.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo electrónico válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}