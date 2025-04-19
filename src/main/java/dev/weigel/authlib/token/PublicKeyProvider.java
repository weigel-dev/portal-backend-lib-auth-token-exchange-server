package dev.weigel.authlib.token;

import java.security.interfaces.RSAPublicKey;

public interface PublicKeyProvider {
    RSAPublicKey getPublicKey();
}
