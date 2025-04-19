package dev.weigel.authlib.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthResponse {

    @JsonProperty(value = "accessToken", required = true)
    private final String accessToken;

    @JsonProperty(value = "refreshToken", required = true)
    private final String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
