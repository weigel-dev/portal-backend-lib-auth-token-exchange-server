package dev.weigel.authlib.controller.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshResponse extends AuthResponse {

    public RefreshResponse(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }

}
