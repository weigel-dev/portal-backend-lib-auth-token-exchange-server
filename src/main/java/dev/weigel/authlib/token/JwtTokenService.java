package dev.weigel.authlib.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import dev.weigel.authlib.exception.TokenCreationException;
import dev.weigel.authlib.exception.TokenHashingException;
import dev.weigel.authlib.service.ClaimsProvider;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtTokenService implements TokenService {

    private final ClaimsProvider claimsProvider;
    private final RSASSASigner signer;
    private final TokenConfiguration tokenConfiguration;

    public JwtTokenService(ClaimsProvider claimsProvider, TokenConfiguration tokenConfiguration) {
        this.claimsProvider = claimsProvider;
        this.tokenConfiguration = tokenConfiguration;

        this.signer = new RSASSASigner(tokenConfiguration.getPrivateKey());
    }

    @Override
    public String createAccessToken(String internalUserId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(tokenConfiguration.getAccessTokenValiditySeconds());

        Map<String, String> claims = claimsProvider.provideClaims(internalUserId);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(internalUserId)
                .issuer(tokenConfiguration.getIssuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration));

        for (Map.Entry<String, String> entry : claims.entrySet()) {
            claimsBuilder.claim(entry.getKey(), entry.getValue());
        }

        try {
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(tokenConfiguration.getKeyId())
                            .build(),
                    claimsBuilder.build());

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new TokenCreationException("Failed to create access token", e);
        }

    }

    @Override
    public String createRefreshToken(String internalUserId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(tokenConfiguration.getRefreshTokenValiditySeconds());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(internalUserId)
                .issuer(tokenConfiguration.getIssuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(tokenConfiguration.getKeyId())
                            .build(),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new TokenCreationException("Failed to create refresh token", e);
        }
    }

    @Override
    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new TokenHashingException("Failed to hash token", e);
        }

    }
}
