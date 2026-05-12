package com.unbosque.mundial_hub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesResponse {
    private List<String> favoriteTeams;
    private List<String> favoriteCities;
    private List<String> favoriteStadiums;
}