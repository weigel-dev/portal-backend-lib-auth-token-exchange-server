package dev.weigel.authlib.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalAuthenticationResult {

    private final boolean validated;
    private final String externalUserId;
    private final String email;

}
