package dev.weigel.authlib.token;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenConfiguration {

    private final String issuer;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final String keyId;

}
