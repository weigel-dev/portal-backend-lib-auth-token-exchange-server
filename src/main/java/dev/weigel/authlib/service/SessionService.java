package dev.weigel.authlib.service;

import java.util.Map;

import dev.weigel.authlib.service.model.SessionResult;
import jakarta.annotation.Nullable;

public interface SessionService {

        SessionResult createSession(String userId,
                        String externalUserId,
                        String provider);

        SessionResult updateSession(String userId,
                        String newRefreshTokenHash,
                        @Nullable Map<String, String> metadata);

        SessionResult validateRefreshToken(String refreshTokenHash,
                        @Nullable Map<String, String> metadata);

        void revokeSession(String refreshToken);

}
