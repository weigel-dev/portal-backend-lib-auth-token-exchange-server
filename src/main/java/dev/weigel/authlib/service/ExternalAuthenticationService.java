package dev.weigel.authlib.service;

import dev.weigel.authlib.service.model.ExternalAuthenticationResult;

public interface ExternalAuthenticationService {

    ExternalAuthenticationResult validate(String externalToken);

}
