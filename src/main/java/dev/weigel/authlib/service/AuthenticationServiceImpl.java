package dev.weigel.authlib.service;

import dev.weigel.authlib.controller.model.AuthRequest;
import dev.weigel.authlib.controller.model.AuthResponse;
import dev.weigel.authlib.controller.model.RefreshRequest;
import dev.weigel.authlib.controller.model.RefreshResponse;
import dev.weigel.authlib.exception.ExternalAuthenticationFailedException;
import dev.weigel.authlib.service.model.ExternalAuthenticationResult;
import dev.weigel.authlib.service.model.SessionResult;
import dev.weigel.authlib.token.TokenService;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ExternalAuthenticationProviderResolver authProviderResolver;
    private final TokenService tokenService;
    private final SessionService sessionService;
    private final UserMappingProvider userMappingProvider;

    public AuthenticationServiceImpl(
            ExternalAuthenticationProviderResolver authProviderResolver,
            TokenService tokenService,
            SessionService sessionService,
            UserMappingProvider userMappingProvider) {
        this.authProviderResolver = authProviderResolver;
        this.tokenService = tokenService;
        this.sessionService = sessionService;
        this.userMappingProvider = userMappingProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        ExternalAuthenticationService externalAuthService = authProviderResolver.resolve(request.getExternalProvider());

        ExternalAuthenticationResult result = externalAuthService.validate(request.getExternalToken());

        if (!result.isValidated()) {
            throw new ExternalAuthenticationFailedException(
                    "External authentication failed for provider: " + request.getExternalProvider());
        }

        String externalUserId = result.getExternalUserId();
        String internalUserId = userMappingProvider.resolveInternalUserId(
                request.getExternalProvider(),
                externalUserId,
                result.getEmail());

        SessionResult session = sessionService.createSession(
                internalUserId,
                externalUserId,
                request.getExternalProvider());

        String accessToken = tokenService.createAccessToken(session);
        String refreshToken = tokenService.createRefreshToken(session);

        String refreshTokenHash = tokenService.hashToken(refreshToken);
        sessionService.updateSession(session.getInternalUserId(), refreshTokenHash, request.getMetadata());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public RefreshResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String refreshTokenHash = tokenService.hashToken(refreshToken);
        SessionResult session = sessionService.validateRefreshToken(refreshTokenHash, request.getMetadata());

        String newAccessToken = tokenService.createAccessToken(session);
        String newRefreshToken = tokenService.createRefreshToken(session);

        String newRefreshTokenHash = tokenService.hashToken(newRefreshToken);
        sessionService.updateSession(session.getInternalUserId(), newRefreshTokenHash, request.getMetadata());

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }

}
