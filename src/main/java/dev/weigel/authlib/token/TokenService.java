package dev.weigel.authlib.token;

import dev.weigel.authlib.service.model.SessionResult;

public interface TokenService {

    String createAccessToken(SessionResult session);

    String createRefreshToken(SessionResult session);

    String hashToken(String token);

}
