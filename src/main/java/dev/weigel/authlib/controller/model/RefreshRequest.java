package dev.weigel.authlib.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {

    @JsonProperty(value = "refreshToken", required = true)
    private String refreshToken;

}
