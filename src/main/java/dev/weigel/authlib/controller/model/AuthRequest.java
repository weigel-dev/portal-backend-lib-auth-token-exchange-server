package dev.weigel.authlib.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @JsonProperty(value = "externalToken", required = true)
    private String externalToken;

    @JsonProperty(value = "externalProvider", required = true)
    private String externalProvider;

}
