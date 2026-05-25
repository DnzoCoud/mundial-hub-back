package com.unbosque.mundial_hub.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferencesRequest {

    @NotNull(message = "La lista de equipos favoritos no puede ser nula")
    private List<String> favoriteTeams;

    @NotNull(message = "La lista de ciudades favoritas no puede ser nula")
    private List<String> favoriteCities;

    @NotNull(message = "La lista de estadios favoritos no puede ser nula")
    private List<String> favoriteStadiums;
}