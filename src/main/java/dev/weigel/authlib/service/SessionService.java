package dev.weigel.authlib.service;

public interface SessionService {

    void createSession(String userId, String externalUserId, String provider, String refreshTokenHash);

    void updateSession(String userId, String newRefreshTokenHash);

    String validateRefreshToken(String refreshTokenHash);

    void revokeSession(String refreshToken);
}
