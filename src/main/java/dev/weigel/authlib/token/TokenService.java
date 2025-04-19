package dev.weigel.authlib.token;

public interface TokenService {

    String createAccessToken(String token);

    String createRefreshToken(String token);

    String hashToken(String token);

}
